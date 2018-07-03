package cn.partytime.listener;

import cn.partytime.cache.danmu.DanmuCacheService;
import cn.partytime.common.cachekey.danmu.DanmuCacheKey;
import cn.partytime.common.util.ListUtils;
import cn.partytime.config.DanmuChannelRepository;
import cn.partytime.handlerThread.FilmDanmuHandler;
import cn.partytime.model.AdminTaskModel;
import cn.partytime.redis.service.RedisService;
import cn.partytime.service.ManagerCachService;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component("filmDanmuListener")
public class FilmDanmuListener  implements MessageListener {


    @Autowired
    private DanmuChannelRepository danmuChannelRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ManagerCachService managerCachService;

    @Autowired
    private DanmuCacheService danmuCacheService;

    @Autowired
    private FilmDanmuHandler filmDanmuHandler;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        if (message != null) {
            String partyId = JSON.parseObject(String.valueOf(message), String.class).replace("'", "");
            Object object = redisService.popFromList(DanmuCacheKey.SEND_FILM_DANMU_CACHE_LIST);
            List<Channel> channelList = danmuChannelRepository.findAdminTaskModelFilmChannelList();
            if (object != null) {
                String acceptMessage = String.valueOf(object);
                log.info("推送给管理员的消息:{}", acceptMessage);
                //QueueDanmuModel queueDanmuModel = JSON.parseObject(acceptMessage, QueueDanmuModel.class);
                Map<String,Object> danmuMap = (Map<String,Object>)JSON.parse(acceptMessage);
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("type", "normalDanmu");
                map.put("data", danmuMap);
                //String msg = JSON.toJSONString(map);
                if(ListUtils.checkListIsNull(channelList)){
                    danmuCacheService.setFilmDanmuToTempList(map);
                }else{
                    filmDanmuHandler.pushDanmuToManager(map, partyId, channelList);
                }
            }
        }
    }

}
