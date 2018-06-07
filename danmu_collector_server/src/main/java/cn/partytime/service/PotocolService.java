package cn.partytime.service;

import cn.partytime.business.danmu.DanmuCommandBussinessService;
import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.cache.client.ClientInfoCacheService;
import cn.partytime.cache.collector.CollectorCacheService;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.ClientConst;
import cn.partytime.common.constants.PotocolComTypeConst;
import cn.partytime.common.util.BooleanUtils;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.config.DanmuChannelRepository;
import cn.partytime.dataRpc.RpcDanmuService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.dataRpc.RpcWechatService;
import cn.partytime.model.DanmuClientInfoModel;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.WechatUserDto;
import cn.partytime.model.WechatUserInfoDto;
import cn.partytime.util.PotocolTypeConst;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lENOVO on 2016/8/31.
 */

@Slf4j
@Service
public class PotocolService {

    @Autowired
    private DanmuChannelRepository danmuChannelRepository;

    @Autowired
    private ScreenDanmuService screenDanmuService;

    @Autowired
    private RpcWechatService rpcWechatService;

    @Autowired
    private RpcPartyService rpcPartyService;

    @Autowired
    private ClientCacheService clientCacheService;

    @Autowired
    private RpcDanmuService rpcDanmuService;


    @Autowired
    private AlarmCacheService alarmCacheService;

    @Autowired
    private CollectorCacheService collectorCacheService;

    @Autowired
    private DanmuCommandBussinessService danmuCommandBussinessService;


    @Autowired
    private DanmuSendService danmuSendService;


    @Autowired
    private ClientCommandService clientCommandService;

    @Value("${netty.host}")
    private String host;

    @Value("${netty.port}")
    private int port;

    @Autowired
    private ClientInfoCacheService clientInfoCacheService;

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
            log.info("当前接收的是移动端提交的信息");
            moblieClientHandler(map, channel);
        }else if (ClientConst.CLIENT_TYPE_JAVACLIENT.equals(clientType)) {
            javaClientHandler(map,channel);
        }else if (ClientConst.CLIENT_TYPE_NODECLIENT.equals(clientType)) {
            nodeClientHandler(map,channel);
        }

    }

    private void javaClientHandler(Map<String,Object> map, Channel channel) {
        String type = String.valueOf(map.get("type"));
        DanmuClientInfoModel clientInfoModel = danmuChannelRepository.get(channel);
        if (PotocolTypeConst.POTOCOL_PING.equals(type)) {
            if(clientInfoModel==null){
                forceLogout(channel);
            }else{
                log.info("当前客户JAVACLIENT端信息:{}接受ping",JSON.toJSONString(clientInfoModel));
            }

        }else if(PotocolComTypeConst.COMMANDTYPE_STARTSTAGEANDFULL.equals(type)){
            log.info("转发给flash的协议:{}",JSON.toJSONString(map));
            String addressId = clientInfoModel.getAddressId();
            map.put("clientType","0");
            //danmuSendService.repeatCommandToAllScreenClient(addressId,JSON.toJSONString(map),0);
        }  else {

            log.info("客户端JAVACLIENT发送给服务器信息:{},不处理", JSON.toJSONString(map));
        }
    }

    private void moblieClientHandler(Map<String,Object> map, Channel channel) {
        log.info("手机端登录服务器,客户端发送给服务器信息:{}", JSON.toJSONString(map));
        String type = String.valueOf(map.get("type"));
        if (PotocolTypeConst.POTOCOL_LOGIN.equals(type)) {
            String openId = String.valueOf(map.get("code"));
            log.info("手机端登录服务器,发送的唯一标示:{}", openId);
            if (StringUtils.isEmpty(openId)) {
                log.info("手机端为非法用户，强制下线");
                forceLogout(channel);
            }
            //判断微信用户是否合法
            WechatUserDto wechatUser = rpcWechatService.findByOpenId(openId);
            log.info("当前登录的手机用户信息:{}", JSON.toJSONString(wechatUser));
            WechatUserInfoDto wechatuserInfo = rpcWechatService.findByWechatId(wechatUser.getId());
            PartyLogicModel partyLogicModel = rpcPartyService.findPartyByLonLat(wechatuserInfo.getLastLongitude(), wechatuserInfo.getLastLatitude());
            //如果活动不存在，不做任何处理
            if (partyLogicModel == null) {
                forceLogout(channel);
                return;
            }
            if (wechatUser != null) {
                openId = wechatUser.getOpenId();
                //将客户端信息与Channel绑定
                DanmuClientInfoModel danmuClientInfoModel = new DanmuClientInfoModel();
                danmuClientInfoModel.setDanmuClientCode(openId);
                danmuClientInfoModel.setAddressId(partyLogicModel.getAddressId());
                danmuClientInfoModel.setClientType(Integer.parseInt(map.get("clientType")+""));

                log.info("绑定通道与客户端对象的关系");
                danmuChannelRepository.set(channel, danmuClientInfoModel);

            } else {
                forceLogout(channel);
            }
        }else if (PotocolTypeConst.POTOCOL_PING.equals(type)) {
            DanmuClientInfoModel clientInfoModel = danmuChannelRepository.get(channel);
            log.info("当前客户端信息:{}接受ping",clientInfoModel.getScreenId());

            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("type", PotocolTypeConst.POTOCOL_PONG);
            String msg = JSON.toJSONString(resultMap);
            log.info("返回给客户端信息{}：" + msg);
            channel.writeAndFlush(new TextWebSocketFrame(msg));
        }else if (PotocolTypeConst.POTOCOL_CLOSE.equals(type)) {
            DanmuClientInfoModel clientInfoModel = danmuChannelRepository.get(channel);
            log.info("关闭客户端：{}",JSON.toJSONString(clientInfoModel));
            forceLogout(channel);
        }
    }

    private void nodeClientHandler(Map<String,Object> map, Channel channel) {
        String type = String.valueOf(map.get("type"));
        boolean isCallBack = BooleanUtils.objectConvertToBoolean(String.valueOf(map.get("isCallBack")));
        log.info("isCallBack===================>"+isCallBack+JSON.toJSONString(map));
        DanmuClientInfoModel clientInfoModel = danmuChannelRepository.get(channel);
        String addressId = clientInfoModel.getAddressId();
        if(isCallBack){
            log.info("收到客户端返回的弹幕信息:{}",JSON.toJSONString(map));
            Object object = map.get("danmuId");
            if(object!=null){
                String danmuId = String.valueOf(object);
                log.info("客户端返回的弹幕编号：{}",danmuId);

                rpcDanmuService.updateDanmuStatus(danmuId,2);

                danmuCommandBussinessService.removePayDanmuSendSuccessQueueSize(addressId,danmuId);
            }
        }else if(PotocolComTypeConst.COMMANDTYPE_PARTY_STATUS.equals(type)){
            log.info("收到客户端返回的状态信息:{}",JSON.toJSONString(map));
            int status = Integer.parseInt(String.valueOf(map.get("status")));
            String partyId = String.valueOf(map.get("partyId"));
            Map<String,Object> commandMap = clientCacheService.getFirstCommandFromCache(addressId);
            if(commandMap!=null){
                Map<String, Object> dataMap = (Map<String, Object>) JSON.parse(String.valueOf(commandMap.get("data")));
                log.info("data:{}",JSON.toJSONString(dataMap));
                int cacheStatus = Integer.parseInt(String.valueOf(dataMap.get("status")));
                String cachePartyId = String.valueOf(dataMap.get("partyId"));
                if(status == cacheStatus && partyId.equals(cachePartyId)){
                    clientCacheService.removeFirstCommandFromCache(addressId);
                    clientCacheService.removeTempCommandCount(addressId);
                }
            }

        } else if(PotocolTypeConst.POTOCOL_DANMU_COUNT.equals(type)){
            String dataStr = JSON.toJSONString(map.get("data"));
            Integer danmuCount = IntegerUtils.objectConvertToInt(dataStr);
            //当返回的弹幕数量大于0的处理
            if(danmuCount>0){
                if(danmuCount<15){
                    //清除弹幕过量的缓存 和 和时间
                    alarmCacheService.removeAlarmCount(addressId, AlarmKeyConst.ALARM_KEY_DANMUEXCESS);
                    alarmCacheService.addAlarmTime(DateUtils.getCurrentDate().getTime(),0,addressId,AlarmKeyConst.ALARM_KEY_DANMUEXCESS);
                }
                //清除没有弹幕的计数 和 时间
                alarmCacheService.removeAlarmCount(addressId,AlarmKeyConst.ALARM_KEY_SYSTEMERROR);
                alarmCacheService.addAlarmTime(DateUtils.getCurrentDate().getTime(),0,addressId,AlarmKeyConst.ALARM_KEY_SYSTEMERROR);
            }
            log.info("==========================================接收客户端返回的弹幕数量:{}",danmuCount);
            String partyId = String.valueOf(map.get("partyId"));
            if(partyId!=null){
                clientInfoModel.setPartyId(partyId);
            }
            danmuChannelRepository.set(channel,clientInfoModel);
            screenDanmuService.setScreenDanmuCount(addressId,danmuCount);
            //将客户端信息存入缓存
            clientInfoCacheService.setClientRegisterCodeIntoSortSet(addressId,ClientConst.CLIENT_TYPE_NODECLIENT,clientInfoModel.getRegistCode(),clientInfoModel.getScreenId());
        }else if (PotocolTypeConst.POTOCOL_PING.equals(type)) {
            log.info("当前客户端信息:{}接受ping",clientInfoModel.getScreenId());

            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("type", PotocolTypeConst.POTOCOL_PONG);
            String msg = JSON.toJSONString(resultMap);
            log.info("返回给客户端信息{}：" + msg);
            channel.writeAndFlush(new TextWebSocketFrame(msg));
        } else if(PotocolComTypeConst.COMMANDTYPE_STARTSTAGEANDFULL.equals(type)){
            log.info("转发给java的协议:{}",JSON.toJSONString(map));
            map.put("clientType","2");
            //clientCommandService.pubCommandToJavaClient(addressId,JSON.toJSONString(map));
        }  else if(PotocolComTypeConst.COMMANDTYPE_CLIENTINFO.equals(type)){
            String ip = String.valueOf(map.get("ip"));
            String port = String.valueOf(map.get("port"));
            int screenId = IntegerUtils.objectConvertToInt(map.get("number"));
            clientInfoModel.setIp(ip);
            clientInfoModel.setScreenId(screenId);
            clientInfoModel.setPort(port);
            danmuChannelRepository.set(channel,clientInfoModel);
            clientInfoCacheService.setClientRegisterCodeIntoSortSet(addressId,ClientConst.CLIENT_TYPE_NODECLIENT,clientInfoModel.getRegistCode(),screenId);
        }else {
            log.info("客户端发送给服务器信息:{},不处理", JSON.toJSONString(map));
        }
    }


    private void screenClientHandler(Map<String,Object> map, Channel channel) {
        String type = String.valueOf(map.get("type"));
        boolean isCallBack = BooleanUtils.objectConvertToBoolean(String.valueOf(map.get("isCallBack")));
        log.info("isCallBack===================>"+isCallBack+JSON.toJSONString(map));
        DanmuClientInfoModel clientInfoModel = danmuChannelRepository.get(channel);
        String addressId = clientInfoModel.getAddressId();
        if(isCallBack){
            log.info("收到客户端返回的弹幕信息:{}",JSON.toJSONString(map));
            Object object = map.get("danmuId");
            if(object!=null){
                String danmuId = String.valueOf(object);
                log.info("客户端返回的弹幕编号：{}",danmuId);

                rpcDanmuService.updateDanmuStatus(danmuId,2);

                danmuCommandBussinessService.removePayDanmuSendSuccessQueueSize(addressId,danmuId);
            }
        }else if(PotocolComTypeConst.COMMANDTYPE_PARTY_STATUS.equals(type)){
            log.info("收到客户端返回的状态信息:{}",JSON.toJSONString(map));
            int status = Integer.parseInt(String.valueOf(map.get("status")));
            String partyId = String.valueOf(map.get("partyId"));
            Map<String,Object> commandMap = clientCacheService.getFirstCommandFromCache(addressId);
            if(commandMap!=null){
                Map<String, Object> dataMap = (Map<String, Object>) JSON.parse(String.valueOf(commandMap.get("data")));
                log.info("data:{}",JSON.toJSONString(dataMap));
                int cacheStatus = Integer.parseInt(String.valueOf(dataMap.get("status")));
                String cachePartyId = String.valueOf(dataMap.get("partyId"));
                if(status == cacheStatus && partyId.equals(cachePartyId)){
                    clientCacheService.removeFirstCommandFromCache(addressId);
                    clientCacheService.removeTempCommandCount(addressId);
                }
            }

        } else if(PotocolTypeConst.POTOCOL_DANMU_COUNT.equals(type)){
            String dataStr = JSON.toJSONString(map.get("data"));
            Integer danmuCount = IntegerUtils.objectConvertToInt(dataStr);
            //当返回的弹幕数量大于0的处理
            if(danmuCount>0){
                if(danmuCount<15){
                    //清除弹幕过量的缓存 和 和时间
                    alarmCacheService.removeAlarmCount(addressId, AlarmKeyConst.ALARM_KEY_DANMUEXCESS);
                    alarmCacheService.addAlarmTime(DateUtils.getCurrentDate().getTime(),0,addressId,AlarmKeyConst.ALARM_KEY_DANMUEXCESS);
                }
                //清除没有弹幕的计数 和 时间
                alarmCacheService.removeAlarmCount(addressId,AlarmKeyConst.ALARM_KEY_SYSTEMERROR);
                alarmCacheService.addAlarmTime(DateUtils.getCurrentDate().getTime(),0,addressId,AlarmKeyConst.ALARM_KEY_SYSTEMERROR);
            }
            log.info("==========================================接收客户端返回的弹幕数量:{}",danmuCount);
            String partyId = String.valueOf(map.get("partyId"));
            if(partyId!=null){
                clientInfoModel.setPartyId(partyId);
            }
            danmuChannelRepository.set(channel,clientInfoModel);
            screenDanmuService.setScreenDanmuCount(addressId,danmuCount);
            //将客户端信息存入缓存
            clientInfoCacheService.setClientRegisterCodeIntoSortSet(addressId,ClientConst.CLIENT_TYPE_SCREEN,clientInfoModel.getRegistCode(),clientInfoModel.getScreenId());
        }else if (PotocolTypeConst.POTOCOL_PING.equals(type)) {
            log.info("当前客户端信息:{}接受ping",clientInfoModel.getScreenId());

            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("type", PotocolTypeConst.POTOCOL_PONG);
            String msg = JSON.toJSONString(resultMap);
            log.info("返回给客户端信息{}：" + msg);
            channel.writeAndFlush(new TextWebSocketFrame(msg));
        } else if(PotocolComTypeConst.COMMANDTYPE_STARTSTAGEANDFULL.equals(type)){
            log.info("转发给java的协议:{}",JSON.toJSONString(map));
            map.put("clientType","2");
            //clientCommandService.pubCommandToJavaClient(addressId,JSON.toJSONString(map));
        }  else if(PotocolComTypeConst.COMMANDTYPE_CLIENTINFO.equals(type)){
            String ip = String.valueOf(map.get("ip"));
            String port = String.valueOf(map.get("port"));
            int screenId = IntegerUtils.objectConvertToInt(map.get("number"));
            clientInfoModel.setIp(ip);
            clientInfoModel.setScreenId(screenId);
            clientInfoModel.setPort(port);
            danmuChannelRepository.set(channel,clientInfoModel);
            clientInfoCacheService.setClientRegisterCodeIntoSortSet(addressId,ClientConst.CLIENT_TYPE_SCREEN,clientInfoModel.getRegistCode(),screenId);
        }else {
            log.info("客户端发送给服务器信息:{},不处理", JSON.toJSONString(map));
        }
    }


    /**
     * 用户断开处理
     *
     * @param channel
     */
    public void forceLogout(Channel channel) {
        DanmuClientInfoModel danmuClientInfoModel = danmuChannelRepository.get(channel);

        if(danmuClientInfoModel !=null && danmuClientInfoModel.getClientType()==0){
            String addressId = danmuClientInfoModel.getAddressId();
            clientCacheService.removeCommandCache(danmuClientInfoModel.getAddressId());

            int count =danmuChannelRepository.findDanmuClientCount(0,addressId);
            log.info("当前场地下载线的flash client数量是:{}",count);
            collectorCacheService.setFlashOfflineTime(addressId);
            collectorCacheService.setClientCount(0,addressId,host,count-1);
            collectorCacheService.setFlahOfflineCLient(addressId, danmuClientInfoModel.getRegistCode());

            //从sorset中清除客户端信息
            String registerCode = danmuClientInfoModel.getRegistCode();
            clientInfoCacheService.removeClientRegisterCodeIntoSortSet(addressId,ClientConst.CLIENT_TYPE_SCREEN,registerCode);
            clientInfoCacheService.removeClientToCache(registerCode,ClientConst.CLIENT_TYPE_SCREEN);

        }



        //清除用户状态
        danmuChannelRepository.remove(channel);

        //关闭通道
        channel.close();
    }

}
