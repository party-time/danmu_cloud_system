package cn.partytime.collector.service;

import cn.partytime.collector.config.DanmuChannelRepository;
import cn.partytime.collector.dataService.PartyLogicService;
import cn.partytime.collector.dataService.WechatUserInfoService;
import cn.partytime.collector.dataService.WechatUserService;
import cn.partytime.collector.model.DanmuClientModel;
import cn.partytime.collector.model.PartyLogicModel;
import cn.partytime.collector.model.WechatUser;
import cn.partytime.collector.model.WechatUserInfo;
import cn.partytime.collector.util.PotocolTypeConst;
import cn.partytime.common.cachekey.CommandCacheKey;
import cn.partytime.common.constants.ClientConst;
import cn.partytime.common.constants.PotocolComTypeConst;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by lENOVO on 2016/8/31.
 */

@Service
public class PotocolService {

    private static final Logger logger = LoggerFactory.getLogger(PotocolService.class);

    @Autowired
    private DanmuChannelRepository danmuChannelRepository;


    @Autowired
    private ScreenDanmuService screenDanmuService;

    @Autowired
    private RedisService redisService;


    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private WechatUserInfoService wechatUserInfoService;


    @Autowired
    private PartyLogicService partyLogicService;

    /**
     * 协议处理
     *
     */
    public void potocolHandler(Map<String,Object> map, Channel channel) {
        //屏幕段处理
        String clientType = String.valueOf(map.get("clientType"));
        if (ClientConst.CLIENT_TYPE_SCREEN.equals(clientType)) {
            screenClientHandler(map, channel);
        } else if (ClientConst.CLIENT_TYPE_MOBILE.equals(clientType)) {
            //移动端弹幕处理
            logger.info("当前接收的是移动端提交的信息");
            moblieClientHandler(map, channel);
        }
    }


    private void moblieClientHandler(Map<String,Object> map, Channel channel) {
        logger.info("手机端登录服务器,客户端发送给服务器信息:{}", JSON.toJSONString(map));
        String type = String.valueOf(map.get("type"));
        if (PotocolTypeConst.POTOCOL_LOGIN.equals(type)) {
            String openId = String.valueOf(map.get("code"));
            logger.info("手机端登录服务器,发送的唯一标示:{}", openId);
            if (StringUtils.isEmpty(openId)) {
                logger.info("手机端为非法用户，强制下线");
                forceLogout(channel);
            }
            //判断微信用户是否合法
            WechatUser wechatUser = wechatUserService.findByOpenId(openId);
            logger.info("当前登录的手机用户信息:{}", JSON.toJSONString(wechatUser));
            WechatUserInfo wechatuserInfo = wechatUserInfoService.findByWechatId(wechatUser.getId());
            PartyLogicModel partyLogicModel = partyLogicService.findPartyByLonLat(wechatuserInfo.getLastLongitude(), wechatuserInfo.getLastLatitude());
            //如果活动不存在，不做任何处理
            if (partyLogicModel == null) {
                forceLogout(channel);
                return;
            }
            if (wechatUser != null) {
                openId = wechatUser.getOpenId();
                //将客户端信息与Channel绑定
                DanmuClientModel danmuClientModel = new DanmuClientModel();
                danmuClientModel.setDanmuClientCode(openId);
                danmuClientModel.setAddressId(partyLogicModel.getAddressId());
                danmuClientModel.setClientType(Integer.parseInt(map.get("clientType")+""));

                logger.info("绑定通道与客户端对象的关系");
                danmuChannelRepository.set(channel, danmuClientModel);

            } else {
                forceLogout(channel);
            }
        }
    }
    private void screenClientHandler(Map<String,Object> map, Channel channel) {
        String type = String.valueOf(map.get("type"));

        //{"type":"danmucount","clientType":"0","code":"m6o844","partyId":"5951ffe2f0b04a04fc030200","data":1}
        if(PotocolComTypeConst.COMMANDTYPE_PARTY_STATUS.equals(type)){
            logger.info("收到客户端返回的状态信息:{}",JSON.toJSONString(map));
            int status = Integer.parseInt(String.valueOf(map.get("status")));
            DanmuClientModel clientInfoModel = danmuChannelRepository.get(channel);
            String key = CommandCacheKey.PUB_COMMAND_PARTYSTATUS_PRE_QUEUE_CACHE+clientInfoModel.getAddressId();
            Set<String> stringSet = redisService.getSortSetByRnage(key,0,100,true);
            if(stringSet!=null && stringSet.size()>0){
                Iterator<String> it  = stringSet.iterator();
                while (it.hasNext()){
                    Map<String, Object> commandMap = (Map<String, Object>) JSON.parse(String.valueOf(it.next()));
                    Map<String, Object> dataObject = (Map<String, Object>) JSON.parse(String.valueOf(commandMap.get("data")));
                    int statusTemp = Integer.parseInt(String.valueOf(dataObject.get("status")));
                    if(status==statusTemp){
                        logger.info("移除命令:{}",JSON.toJSONString(commandMap));
                        it.remove();
                        redisService.deleteSortData(key,JSON.toJSONString(commandMap));
                    }
                }
            }

        } else if(PotocolTypeConst.POTOCOL_DANMU_COUNT.equals(type)){

            DanmuClientModel clientInfoModel = danmuChannelRepository.get(channel);
            String addressId = clientInfoModel.getAddressId();
            String dataStr = JSON.toJSONString(map.get("data"));
            Integer danmuCount = IntegerUtils.objectConvertToInt(dataStr);
            logger.info("==========================================接收客户端返回的弹幕数量:{}",danmuCount);
            String partyId = String.valueOf(map.get("partyId"));
            if(partyId!=null){
                clientInfoModel.setPartyId(partyId);
            }
            danmuChannelRepository.set(channel,clientInfoModel);
            screenDanmuService.setScreenDanmuCount(addressId,danmuCount);

        }else if (PotocolTypeConst.POTOCOL_PING.equals(type)) {
            DanmuClientModel clientInfoModel = danmuChannelRepository.get(channel);
            logger.info("当前客户端信息:{}接受ping",clientInfoModel.getScreenId());

            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("type", PotocolTypeConst.POTOCOL_PONG);
            String msg = JSON.toJSONString(resultMap);
            logger.info("返回给客户端信息{}：" + msg);
            channel.writeAndFlush(new TextWebSocketFrame(msg));
        } else {

            logger.info("客户端发送给服务器信息:{},不处理", JSON.toJSONString(map));
        }
    }


    /**
     * 用户断开处理
     *
     * @param channel
     */
    public void forceLogout(Channel channel) {

        //清除用户状态
        danmuChannelRepository.remove(channel);
        //关闭通道
        channel.close();
    }
}
