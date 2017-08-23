package cn.partytime.listener;

import cn.partytime.common.cachekey.DanmuCacheKey;
import cn.partytime.common.util.ListUtils;
import cn.partytime.config.DanmuChannelRepository;
import cn.partytime.model.AdminTaskModel;
import cn.partytime.redis.service.RedisService;
import cn.partytime.service.ManagerCachService;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("filmDanmuListener")
public class FilmDanmuListener  implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(FilmDanmuListener.class);

    @Autowired
    private DanmuChannelRepository danmuChannelRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ManagerCachService managerCachService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        if (message != null) {
            String partyId = JSON.parseObject(String.valueOf(message), String.class).replace("'", "");
            Object object = redisService.popFromList(DanmuCacheKey.SEND_FILM_DANMU_CACHE_LIST);
            List<Channel> channelList = danmuChannelRepository.findAdminTaskModelFilmChannelList();
            if (object != null) {
                String acceptMessage = String.valueOf(object);
                logger.info("推送给管理员的消息:{}", acceptMessage);
                //QueueDanmuModel queueDanmuModel = JSON.parseObject(acceptMessage, QueueDanmuModel.class);
                Map<String,Object> danmuMap = (Map<String,Object>)JSON.parse(acceptMessage);
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("type", "normalDanmu");
                map.put("data", danmuMap);
                String msg = JSON.toJSONString(map);
                appointTaskToManager(msg, partyId, channelList);
            }
        }
    }
    public void appointTaskToManager(String message, String partyId, List<Channel> channelList) {
        logger.info("开始给管理员分配任务");
        List<AdminTaskModel> adminTaskModelList = new ArrayList<AdminTaskModel>();
        for (Channel channel : channelList) {
            AdminTaskModel adminTaskModel = danmuChannelRepository.findAdminTaskModel(channel);
            String channelId = channel.id().asLongText();
            adminTaskModel.setAdminId(channelId);
            adminTaskModel.setChannel(channel);
            int count = managerCachService.appointTaskCount(channelId);
            adminTaskModel.setCount(count);
            adminTaskModelList.add(adminTaskModel);
            logger.info("管理员信息:{}", JSON.toJSONString(adminTaskModel));
        }

        if (ListUtils.checkListIsNotNull(adminTaskModelList)) {
            Collections.sort(adminTaskModelList, new Comparator<AdminTaskModel>() {
                @Override
                public int compare(AdminTaskModel o1, AdminTaskModel o2) {
                    int i = o1.getCount() - o2.getCount();
                    if (i > 0) {
                        return 1;
                    } else if (i < 0) {
                        return -1;
                    }
                    return 0;
                }
            });
            //TODO:当前管理员的弹幕数大于等于taskCount就不分配弹幕了
            AdminTaskModel adminTaskModel = adminTaskModelList.get(0);

            Channel channel = adminTaskModel.getChannel();
            channel.writeAndFlush(new TextWebSocketFrame(message));
            managerCachService.addAppointCount(adminTaskModel.getAdminId());
        }

    }
}
