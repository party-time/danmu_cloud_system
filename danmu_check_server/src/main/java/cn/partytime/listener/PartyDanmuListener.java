package cn.partytime.listener;

import cn.partytime.cache.danmu.DanmuCacheService;
import cn.partytime.common.cachekey.danmu.DanmuCacheKey;
import cn.partytime.config.DanmuChannelRepository;
import cn.partytime.common.util.ListUtils;
import cn.partytime.handlerThread.PartyDanmuPushHandler;
import cn.partytime.model.AdminTaskModel;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component("partyDanmuListener")
public class PartyDanmuListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(PartyDanmuListener.class);


    @Autowired
    private PartyDanmuPushHandler partyDanmuPushHandler;

    @Autowired
    private DanmuChannelRepository danmuChannelRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private DanmuCacheService danmuCacheService;


    @Override
    public void onMessage(Message message, byte[] bytes) {
        logger.info("收到的消息:{}", message);
        if (message != null) {
            String partyId = JSON.parseObject(String.valueOf(message), String.class).replace("'", "");
            logger.info("处理活动{}弹幕", partyId);
            List<Channel> channelList = danmuChannelRepository.findChannelListByPartyId(partyId);
            Object object = redisService.popFromList(DanmuCacheKey.SEND_DANMU_CACHE_LIST + partyId);
            if (object != null) {
                //danmuCacheService.setPartyDanmuToTempList(partyId, object);
                if (!ListUtils.checkListIsNotNull(channelList)) {
                    danmuCacheService.setPartyDanmuToTempList(partyId, object);
                } else {

                    /*String acceptMessage = String.valueOf(object);
                    log.info("推送给管理员的消息:{}", acceptMessage);
                    Map<String, Object> danmuMap = (Map<String, Object>) JSON.parse(acceptMessage);
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("type", "normalDanmu");
                    map.put("data", danmuMap);
                    String msg = JSON.toJSONString(map);*/

                    partyDanmuPushHandler.pushDanmuToManager(object, channelList,partyId);
                }
            }
        }

    }


}