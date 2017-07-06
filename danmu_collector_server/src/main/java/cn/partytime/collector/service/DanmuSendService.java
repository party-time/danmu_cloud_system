package cn.partytime.collector.service;

import cn.partytime.collector.config.DanmuChannelRepository;
import cn.partytime.collector.model.ProtocolDanmuModel;
import cn.partytime.collector.model.ProtocolModel;
import cn.partytime.common.cachekey.ScreenClientCacheKey;
import cn.partytime.common.constants.*;
import cn.partytime.common.util.*;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lENOVO on 2016/10/10.
 */

@Component
public class DanmuSendService {
    private static final Logger logger = LoggerFactory.getLogger(DanmuSendService.class);

    @Autowired
    private ClientCacheService clientCacheService;

    @Autowired
    private ClientPreDanmuService clientPreDanmuService;

    @Autowired
    private ClientChannelService clientChannelService;


    @Autowired
    private ScreenDanmuService screenDanmuService;


    /**
     * 发送预制弹幕
     *
     * @param addressId
     */
    public void sendPreDanmu(String addressId,String partyId) {
        Date nowDate = DateUtils.getCurrentDate();
        Object preObject = clientPreDanmuService.getPreDanmu(addressId,nowDate,partyId);
        logger.info("获取的预制弹幕信息：{}",JSON.toJSONString(preObject));
        if (preObject == null) {
            return;
        }
        String key = ScreenClientCacheKey.SCREEN_DANMU_COUNT+addressId;

        screenDanmuService.addScreenDanmuCount(addressId);
        sendMessageToAllClient(addressId,preObject);

    }

    /**
     * 发送实时弹幕
     *
     * @param addressId
     */
    public void sendDanmu(String addressId) {
        //接收的初始弹幕
        Object object = clientCacheService.getDanmuFromPubDanmuList(addressId);
        logger.info("从缓存中弹幕信息:{}", object);
        ProtocolModel<ProtocolDanmuModel> protocolModel = JSON.parseObject(String.valueOf(object), ProtocolModel.class);
        //协议类型
        String type = protocolModel.getType();
        logger.info("当前接收的协议类型是{}",type);

        if(ProtocolConst.PROTOCOL_COMMAND.equals(type)){
            //获取的是命令。直接广播给所有屏幕
            pubDanmuToAllScreenClient(addressId, protocolModel);
        }else{
            Map<String,Object> map = (Map<String,Object>)JSON.parse(String.valueOf(object));
            String danmuType = (String)map.get("type");
            logger.info("向所有屏幕广播弹幕:{}",danmuType);
            sendMessageToAllClient(addressId,object);

        }
    }





    public void sendMessageToAllClient(String addressId,Object object){
        int clientType = Integer.parseInt(ClientConst.CLIENT_TYPE_SCREEN);
        List<Channel> screenChannelList = clientChannelService.findDanmuClientChannelAddressByClientType(addressId,clientType);

        if(ListUtils.checkListIsNotNull(screenChannelList)){
            logger.info("当前在线的flash客户端数量是:{}",screenChannelList.size());
            for(Channel channel:screenChannelList){
                String message = String.valueOf(object);
                logger.info("向flash客户端:{},推送弹幕:{}",channel.id(),message);
                channel.writeAndFlush(new TextWebSocketFrame(message));
            }
        }

        Map<String,Object> map = (Map<String,Object>)JSON.parse(String.valueOf(object));


        Object objectMessage =map.get("isSendH5");
        if(objectMessage!=null){
            int isSendH5 = Integer.parseInt(String.valueOf(objectMessage));
            //是否发送到H5界面 0 发送 1不发送
            if(isSendH5==1){
                return;
            }
        }

        clientType = Integer.parseInt(ClientConst.CLIENT_TYPE_MOBILE);
        List<Channel> channelMobileList = clientChannelService.findDanmuClientChannelAddressByClientType(addressId,clientType);

        if(ListUtils.checkListIsNotNull(channelMobileList)){
            logger.info("当前在线的手机客户端数量是:{}",channelMobileList.size());
            for(Channel channel:channelMobileList){
                logger.info("向手机客户端:{},推送弹幕",channel.id());
                channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(object)));
            }
        }
    }



    /**
     * 向所有屏幕广播弹幕
     *
     * @param addressId
     * @param protocolModel
     */
    public void pubDanmuToAllScreenClient(String addressId, ProtocolModel protocolModel) {
        logger.info("向地址{}所有屏幕广播弹幕", addressId);
        int clientType = Integer.parseInt(ClientConst.CLIENT_TYPE_SCREEN);
        List<Channel> screenChannelList = clientChannelService.findDanmuClientChannelAddressByClientType(addressId,clientType);
        if (!ListUtils.checkListIsNotNull(screenChannelList)) {
            return;
        }
        for (Channel channel : screenChannelList) {
            logger.info("向{}广播弹幕时间{}", channel.id(), DateUtils.getCurrentDate().getTime());
            String message = JSON.toJSONString(protocolModel);
            channel.writeAndFlush(new TextWebSocketFrame(message));
        }
    }

    public void pubDanmuToAllScreenClient(String addressId, String message) {
        logger.info("向地址{}所有屏幕广播弹幕", addressId);
        int clientType = Integer.parseInt(ClientConst.CLIENT_TYPE_SCREEN);
        List<Channel> screenChannelList = clientChannelService.findDanmuClientChannelAddressByClientType(addressId,clientType);
        if (!ListUtils.checkListIsNotNull(screenChannelList)) {
            return;
        }
        for (Channel channel : screenChannelList) {
            channel.writeAndFlush(new TextWebSocketFrame(message));
        }
    }


}
