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
            Object object = redisService.popFromList(DanmuCacheKey.SEND_FILM_DANMU_CACHE_LIST);
            List<Channel> channelList = danmuChannelRepository.findAdminTaskModelFilmChannelList();
            log.info("监听到电影场有人发弹幕：{}",JSON.toJSONString(object));
            if (object != null) {
                if(ListUtils.checkListIsNull(channelList)){
                    danmuCacheService.setFilmDanmuToTempList(object);
                }else{
                    filmDanmuHandler.pushDanmuToManager(object,channelList);
                }
            }
        }
    }

}
