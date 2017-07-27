package cn.partytime.service;

import cn.partytime.common.constants.ClientConst;
import cn.partytime.common.constants.PotocolComTypeConst;
import cn.partytime.common.constants.ProtocolConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.config.DanmuChannelRepository;
import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.dataRpc.RpcDanmuClientService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.dataRpc.RpcWechatService;
import cn.partytime.model.*;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lENOVO on 2016/12/2.
 */


@Service
public class ClientLoginService {

    private static final Logger logger = LoggerFactory.getLogger(ClientLoginService.class);


    @Autowired
    private DanmuChannelRepository danmuChannelRepository;

    @Autowired
    private RpcDanmuAddressService rpcDanmuAddressService;

    @Autowired
    private RpcPartyService partyService;

    @Autowired
    private RpcDanmuClientService rpcDanmuClientService;

    @Autowired
    private ClientChannelService clientChannelService;

    @Autowired
    private RpcWechatService rpcWechatService;

    @Autowired
    private ScreenDanmuService screenDanmuService;

    @Autowired
    private ClientCacheService clientCacheService;


    /**
     * 客户端登录
     *
     * @param code
     * @param clientType
     * @param channel
     */
    public void clientLogin(String code, String clientType, Channel channel) {
        if (ClientConst.CLIENT_TYPE_SCREEN.equals(clientType)) {
            logger.info("屏幕{}端触发登录", code);
            screenClientLogin(code, channel, clientType);
        } else if (ClientConst.CLIENT_TYPE_MOBILE.equals(clientType)) {
            //移动端弹幕处理
            logger.info("h5{}端触发登录", code);
            moblieClientLogin(code, channel, clientType);
        } else if (ClientConst.CLIENT_TYPE_JAVACLIENT.equals(clientType)) {
            //移动端弹幕处理
            logger.info("java{}端触发登录", code);
            javaClientLogin(code, channel, clientType);
        }
    }


    public void javaClientLogin(String code, Channel channel, String clientType) {
        DanmuClientModel danmuClient = findDanmuClientInfo(code);

        logger.info("将要连接到服务器客户端的信息:{}", JSON.toJSONString(danmuClient));
        if (danmuClient != null) {
            //判断用户是否登陆过
            boolean isLogin = checkClientIsLogin(code,Integer.parseInt(clientType));
            if (isLogin) {
                logger.info("当前客户端已经登录过，下线");
                channel.close();
                return;
            }
            DanmuClientModel danmuClientModel = new DanmuClientModel();
            BeanUtils.copyProperties(danmuClient, danmuClientModel);
            danmuClientModel.setClientType(Integer.parseInt(clientType));
            danmuClientModel.setDanmuCount(0);
            danmuClientModel.setLastTime(DateUtils.getCurrentDate().getTime());

            logger.info("绑定通道与客户端对象的关系");
            danmuChannelRepository.set(channel, danmuClientModel);

            //获取活动信息
            String commandType = PotocolComTypeConst.COMMANDTYPE_PARTY_STATUS;
            PartyLogicModel party = partyService.findPartyAddressId(danmuClientModel.getAddressId());
            //只有是活动场的情况下，此处才有效果
            if (party != null) {
                try{
                    int status = party.getStatus();
                    logger.info("commandType:{},partyId:{},activeTime:{},status:{}",commandType,party.getPartyId(),party.getActiveTime(),status);

                    //ProtocolModel protocolModel = ProtocolUtil.setCommandFilmStartProtocolModel(commandType, party.getPartyId(), party.getStartTime(),party.getActiveTime(),status+"");
                    Map<String,Object> commandObject = new HashMap<>();
                    commandObject.put("type", ProtocolConst.PROTOCOL_COMMAND);

                    Map<String,Object> dataMap = new HashMap<String,Object>();
                    dataMap.put("type",commandType);
                    dataMap.put("status",status);
                    dataMap.put("partyId",party.getPartyId());

                    if(party.getStartTime()!=null){
                        dataMap.put("partyTime",party.getStartTime().getTime());
                    }
                    if(party.getActiveTime()!=null){
                        dataMap.put("movieTime",party.getActiveTime().getTime());
                    }
                    commandObject.put("data",dataMap);

                    String message = JSON.toJSONString(commandObject);
                    logger.info("下发消息给javaclient客户端:{}",message);
                    channel.writeAndFlush(new TextWebSocketFrame(message));
                }catch (Exception e){
                    logger.info("========================>"+e.getMessage());
                }
            }
        }
    }

    /**
     * 手机端登录
     *
     * @param openId
     * @param channel
     * @param type
     */
    private void moblieClientLogin(String openId, Channel channel, String type) {

        logger.info("手机端登录服务器,发送的唯一标示:{}", openId);
        if (StringUtils.isEmpty(openId)) {
            logger.info("手机端为非法用户，强制下线");
            channel.close();
        }
        //判断微信用户是否合法
        WechatUserDto wechatUser = rpcWechatService.findByOpenId(openId);
        logger.info("当前登录的手机用户信息:{}", JSON.toJSONString(wechatUser));
        if (wechatUser == null) {
            logger.info("通过openId:{}获取的微信用户信息为空,用户为非法用户", openId);
            channel.close();
        }

        WechatUserInfoDto wechatUserInfo = rpcWechatService.findByWechatId(wechatUser.getId());
        if (wechatUserInfo == null) {
            logger.info("通过wechatId:{}获取的微信用户地理位置为空,用户为非法用户", wechatUserInfo);
            channel.close();
        }

        DanmuAddressModel danmuAddress = rpcDanmuAddressService.findAddressByLonLat(wechatUserInfo.getLastLongitude(), wechatUserInfo.getLastLatitude());
        logger.info("通过经纬度:{},{}获取地址信息",wechatUserInfo.getLastLongitude(), wechatUserInfo.getLastLatitude(),JSON.toJSONString(danmuAddress));
        //如果查询不到场地
        if (danmuAddress == null) {
            channel.close();
        }
        String addressId = danmuAddress.getId();
        logger.info("手机获取的地址信息：{}",addressId);

        PartyLogicModel party = partyService.findPartyAddressId(addressId);

        logger.info("活动信息：{}",JSON.toJSONString(party));
        //如果活动不存在，不做任何处理
        if (party == null) {
            logger.info("通过地址{}获取不到活动信息，让用户下线", addressId);
            channel.close();
            return;
        }
        //将客户端信息与Channel绑定
        DanmuClientModel danmuClientModel = new DanmuClientModel();
        danmuClientModel.setDanmuClientCode(openId);
        danmuClientModel.setAddressId(addressId);
        danmuClientModel.setClientType(Integer.parseInt(type));
        logger.info("绑定通道与客户端对象的关系");
        danmuChannelRepository.set(channel, danmuClientModel);
    }


    /**
     * 屏幕登录
     *
     * @param code
     * @param channel
     * @param clientType
     */
    public void screenClientLogin(String code, Channel channel, String clientType) {
        DanmuClientModel danmuClient = findDanmuClientInfo(code);

        logger.info("将要连接到服务器客户端的信息:{}", JSON.toJSONString(danmuClient));
        if (danmuClient != null) {
            //判断用户是否登陆过
            boolean isLogin = checkClientIsLogin(code,Integer.parseInt(clientType));
            if (isLogin) {
                logger.info("当前客户端已经登录过，下线");
                channel.close();
                return;
            }
            DanmuClientModel danmuClientModel = new DanmuClientModel();
            BeanUtils.copyProperties(danmuClient, danmuClientModel);
            danmuClientModel.setClientType(Integer.parseInt(clientType));
            danmuClientModel.setDanmuCount(0);
            danmuClientModel.setLastTime(DateUtils.getCurrentDate().getTime());
            screenDanmuService.setScreenDanmuCount(danmuClient.getAddressId(),0);
            logger.info("绑定通道与客户端对象的关系");
            danmuChannelRepository.set(channel, danmuClientModel);

            //将当前客户端的信息存入缓
            clientCacheService.setClientIdIntoCache(danmuClient.getAddressId(),danmuClient.getRegistCode());

            // 获取活动信息
            String commandType = PotocolComTypeConst.COMMANDTYPE_PARTY_STATUS;
            PartyLogicModel party = partyService.findPartyAddressId(danmuClientModel.getAddressId());
            //只有是活动场的情况下，此处才有效果
            if (party != null) {
                try{
                    int status = party.getStatus();
                    logger.info("commandType:{},partyId:{},activeTime:{},status:{}",commandType,party.getPartyId(),party.getActiveTime(),status);
                    Map<String,Object> commandObject = new HashMap<String,Object>();
                    commandObject.put("type", ProtocolConst.PROTOCOL_COMMAND);
                    Map<String,Object> dataMap = new HashMap<String,Object>();
                    dataMap.put("type",commandType);
                    dataMap.put("status",status);
                    dataMap.put("partyId",party.getPartyId());
                    if(party.getStartTime()!=null){
                        dataMap.put("partyTime",party.getStartTime().getTime());
                    }
                    if(party.getActiveTime()!=null){
                        dataMap.put("movieTime",party.getActiveTime().getTime());
                    }
                    commandObject.put("data",dataMap);
                    String message = JSON.toJSONString(commandObject);
                    logger.info("下发消息给客户端:{}",message);
                    channel.writeAndFlush(new TextWebSocketFrame(message));
                }catch (Exception e){
                    logger.info("========================>"+e.getMessage());
                }
            }
        } else {
            logger.info("当前连接的用户未非法用户,强制下线");
            channel.close();
            return;
        }
    }


    public DanmuClientModel findDanmuClientInfo(String danmuClientCode) {
        DanmuClientModel danmuClient = rpcDanmuClientService.findByRegistCode(danmuClientCode);
        logger.info("通过注册码：{},获取客户端信息:{}", danmuClientCode, JSON.toJSONString(danmuClient));
        if (danmuClient != null) {
            return danmuClient;
        }
        return null;
    }

    public boolean checkClientIsLogin(String code,int clientType) {
        logger.info("----------------------------------------{},{}",code,clientType);
        Channel channel = clientChannelService.findChannelByCode(code,clientType);
        if (channel == null) {
            return false;
        }
        return true;
    }

}
