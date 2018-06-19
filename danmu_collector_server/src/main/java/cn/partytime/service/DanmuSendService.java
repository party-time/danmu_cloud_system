package cn.partytime.service;

import cn.partytime.alarmRpc.RpcDanmuAlarmService;
import cn.partytime.business.danmu.DanmuCommandBussinessService;
import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.cache.danmu.PreDanmuCacheService;
import cn.partytime.dataRpc.RpcDanmuClientService;
import cn.partytime.dataRpc.RpcDanmuService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.dataRpc.RpcPreDanmuService;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.PartyModel;
import cn.partytime.model.ProtocolDanmuModel;
import cn.partytime.model.ProtocolModel;
import cn.partytime.common.cachekey.ScreenClientCacheKey;
import cn.partytime.common.constants.*;
import cn.partytime.common.util.*;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
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
    private ClientChannelService clientChannelService;


    @Autowired
    private ScreenDanmuService screenDanmuService;

    @Autowired
    private RpcDanmuAlarmService rpcDanmuAlarmService;

    @Autowired
    private RpcPartyService rpcPartyService;

    @Autowired
    private AlarmCacheService alarmCacheService;


    @Autowired
    private RpcPreDanmuService rpcPreDanmuService;

    @Autowired
    private PreDanmuCacheService preDanmuCacheService;


    @Autowired
    private DanmuCommandBussinessService danmuCommandBussinessService;

    @Value("${whiteColor.addressId:584a1a9a0cf2fdb8406efdce}")
    private String whiteColorAddrssId;


    @Autowired
    private RpcDanmuService rpcDanmuService;

    /**
     * 发送预制弹幕
     *
     * @param addressId
     */
    public void sendPreDanmu(String addressId,String partyId,int danmuCount) {
        Date nowDate = DateUtils.getCurrentDate();

        int density = rpcPreDanmuService.getPartyDanmuDensity(partyId);
        int danmmuClass = density - danmuCount;
        if(danmmuClass>=0){
            Map<String,Object> preDanmuMap= rpcPreDanmuService.getPreDanmuFromCache(partyId,addressId,danmmuClass);

            logger.info("获取的预制弹幕信息：{}",JSON.toJSONString(preDanmuMap));
            if (preDanmuMap == null) {
                //获取活动信息
                PartyModel partyModel =rpcPartyService.getPartyByPartyId(partyId);
                int cacheCount = alarmCacheService.findAlarmCount(addressId,AlarmKeyConst.ALARM_KEY_PREDANMU);
                if(partyModel.getType()==1 && cacheCount==0){

                    long time = preDanmuCacheService.findPreDanmAlarmDelayTime(partyId,addressId);
                    if(time==0){
                        preDanmuCacheService.setPreDanmAlarmDelayTime(partyId,addressId);
                        return;
                    }
                    Date now = DateUtils.getCurrentDate();
                    long subMinute = (now.getTime()-time)/1000/60;
                    if(subMinute>1){
                        rpcDanmuAlarmService.danmuAlarm(AlarmConst.DanmuAlarmType.PRE_DANMU_IS_NULL,addressId,"null");
                        preDanmuCacheService.removePreDanmAlarmDelayTime(partyId,addressId);
                    }

                }
                return;
            }
            screenDanmuService.addScreenDanmuCount(addressId);
            convertMessageToProtocolToClient(addressId,preDanmuMap);
        }


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
            pubMessageToAllClient(addressId, JSON.toJSONString(protocolModel),ClientConst.CLIENT_TYPE_SCREEN);
        }else{
            Map<String,Object> map = (Map<String,Object>)JSON.parse(String.valueOf(object));
            String danmuType = (String)map.get("type");
            logger.info("向所有屏幕广播弹幕:{}",danmuType);
            sendMessageToAllClient(addressId,object);

        }
    }


    public void sendMessageToAllClient(String addressId,Object object){
        Map<String,Object> map = (Map<String,Object>)JSON.parse(String.valueOf(object));
        convertMessageToProtocolToClient(addressId,map);
    }

    public void convertMessageToProtocolToClient(String addressId, Map<String,Object> map){
        Object objectMessage =map.get("isSendH5");
        Object type =map.get("type");
        Object danmuIdObject = map.get("danmuId");
        Object dataObject = map.get("data");
        String danmuId = String.valueOf(danmuIdObject);
        Map<String,Object> dataMap = (Map<String,Object>)JSON.parse(String.valueOf(JSON.toJSONString(dataObject)));
        Object isPayObject = dataMap.get("isPay");
        //给指定的场地弹幕颜色设置成白色
        if(whiteColorAddrssId.equals(addressId) && "pDanmu".equals(String.valueOf(type))){
            logger.info("whiteColorAddrssId:{},给场地：{}弹幕颜色设置成白色",whiteColorAddrssId,addressId);
            dataMap.put("color","0xffffff");
        }
        map.put("data",dataMap);
        //判断是否是支付弹幕
        if(isPayObject!=null){
            boolean isPayFlg = BooleanUtils.objectConvertToBoolean(isPayObject);
            if(isPayFlg){
                //从支付成功，未发送成功队列中清除未发送的支付弹幕
                danmuCommandBussinessService.removePayDanmuNotSendQueueSize(addressId,danmuId);
                //向支付成功发送成功的队列中存入数据
                danmuCommandBussinessService.putIntoPayDanmuSendSuccessQueue(addressId,danmuId);
            }
        }
        String message = String.valueOf(JSON.toJSONString(map));
        //pubDanmuToAllScreenClient(addressId,message);
        pubMessageToAllClient(addressId,message,ClientConst.CLIENT_TYPE_SCREEN);

        pubMessageToAllClient(addressId,message,ClientConst.CLIENT_TYPE_NODECLIENT);

        //设置弹幕发送状态
        rpcDanmuService.updateDanmuStatus(danmuId,1);
        if(objectMessage!=null){
            int isSendH5 = Integer.parseInt(String.valueOf(objectMessage));
            //是否发送到H5界面 0 发送 1不发送
            if(isSendH5==1){
                return;
            }
        }
        //向手机广播弹幕
        pubMessageToAllClient(addressId,message,ClientConst.CLIENT_TYPE_MOBILE);
    }

    /**
     * 向所有客户端广播消息
     * @param addressId
     */
    public void pubMessageToAllClient(String addressId, String message,String type) {

        logger.info("向地址{}客户端{}广播弹幕广播协议:{}", addressId,type,message);
        int clientType = Integer.parseInt(type);
        List<Channel> screenChannelList = clientChannelService.findDanmuClientChannelAddressByClientType(addressId,clientType);
        if (!ListUtils.checkListIsNotNull(screenChannelList)) {
            return;
        }
        for (Channel channel : screenChannelList) {
            logger.info("==================================>{},{}",channel.id(),message);
            channel.writeAndFlush(new TextWebSocketFrame(message));
        }
    }

}
