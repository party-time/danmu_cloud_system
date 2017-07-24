package cn.partytime.listener;

import cn.partytime.service.ClientChannelService;
import cn.partytime.service.PreDanmuLogicService;
import cn.partytime.common.cachekey.CommandCacheKey;
import cn.partytime.common.constants.ClientConst;
import cn.partytime.common.util.ListUtils;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/11.
 */
@Component("partyCommandListener")
public class PartyCommandListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(PartyCommandListener.class);
    @Autowired
    private RedisService redisService;

    @Autowired
    private ClientChannelService clientChannelService;

    @Autowired
    private PreDanmuLogicService preDanmuLogicService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        if (message != null) {
            String addressId = JSON.parseObject(String.valueOf(message), String.class).replace("'", "");
            int flashCount = clientChannelService.findDanmuClientCountByAddressIdAndClientType(addressId, Integer.parseInt(ClientConst.CLIENT_TYPE_SCREEN));
            int javaCount = clientChannelService.findDanmuClientCountByAddressIdAndClientType(addressId, Integer.parseInt(ClientConst.CLIENT_TYPE_JAVACLIENT));
            if (flashCount + javaCount == 0) {
                logger.info("本服务器不处理{}此地址的命令", addressId);
                return;
            }
            //接收的初始弹幕
            String key = CommandCacheKey.PUB_COMMAND_PARTYSTATUS_CACHE + addressId;
            Object object = redisService.get(key);
            logger.info("从缓存中读取命令信息:{}", object);
            if (object != null) {
                Map<String, Object> map = (Map<String, Object>) JSON.parse(String.valueOf(object));
                //协议类型
                String type = String.valueOf(map.get("type"));

                if ("command".equals(type)) {
                    Map<String, Object> dataObject = (Map<String, Object>) JSON.parse(String.valueOf(map.get("data")));
                    if ("partyStatus".equals(dataObject.get("type"))) {

                        int status = Integer.parseInt(String.valueOf(dataObject.get("status")));

                        if (status < 3) {

                            String repeatCommandCacheKey = CommandCacheKey.PUB_COMMAND_PARTYSTATUS_PRE_QUEUE_CACHE+addressId;
                            //设置到预发送队列
                            redisService.setSortSet(repeatCommandCacheKey, status, JSON.toJSONString(map));
                            redisService.expire(repeatCommandCacheKey,60*60*2);


                        }
                        pubDanmuToAllScreenClient(addressId, map);
                        //触发预制弹幕逻辑
                        preDanmuLogicService.sendPreDanmu(addressId, 0, String.valueOf(dataObject.get("partyId")));
                    } else {
                        pubDanmuToAllScreenClient(addressId, map);
                    }
                } else {
                    pubDanmuToAllScreenClient(addressId, map);
                }

            }

            //获取广告弹幕信息
            key = CommandCacheKey.PUB_COMMAND_PROMOTIONALFILM_CACHE + addressId;
            object = redisService.get(key);
            logger.info("从缓存中读取广告命令信息:{}", object);
            if (object != null) {
                Map<String, Object> map = (Map<String, Object>) JSON.parse(String.valueOf(object));
                //协议类型
                logger.info("当前接收的协议类型是{}", map.get("type"));
                pubDanmuToAllScreenClient(addressId, map);
            }

        }
    }

    /**
     * 向所有屏幕广播弹幕
     *
     * @param addressId
     */
    public void pubDanmuToAllScreenClient(String addressId, Map<String, Object> map) {
        logger.info("向地址{}所有屏幕下发命令", addressId);

        List<Channel> screenChannelList = clientChannelService.findDanmuClientChannelAddressByClientType(addressId, Integer.parseInt(ClientConst.CLIENT_TYPE_SCREEN));
        if (ListUtils.checkListIsNotNull(screenChannelList)) {
            logger.info("向flash客户端下发命令");
            for (Channel channel : screenChannelList) {
                Map<String, Object> dataMap = (Map<String, Object>) JSON.parse(String.valueOf(map.get("data")));
                dataMap.put("clientType", 0);
                map.put("type", map.get("type"));
                map.put("data", dataMap);
                String message = JSON.toJSONString(map);
                logger.info("向flash客户端:{}下发命令", channel.id());

                channel.writeAndFlush(new TextWebSocketFrame(message));
            }
        }

        List<Channel> channelList = clientChannelService.findDanmuClientChannelAddressByClientType(addressId, Integer.parseInt(ClientConst.CLIENT_TYPE_JAVACLIENT));
        if (ListUtils.checkListIsNotNull(channelList)) {
            for (Channel channel : channelList) {
                Map<String, Object> dataMap = (Map<String, Object>) JSON.parse(String.valueOf(map.get("data")));
                dataMap.put("clientType", 2);
                map.put("type", map.get("type"));
                map.put("data", dataMap);

                String message = JSON.toJSONString(map);
                logger.info("向javaclient客户端:{}下发命令", channel.id());
                channel.writeAndFlush(new TextWebSocketFrame(message));
            }
        }
    }
}
