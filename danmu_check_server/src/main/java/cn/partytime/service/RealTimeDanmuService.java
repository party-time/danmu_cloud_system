package cn.partytime.service;

import cn.partytime.config.DanmuChannelRepository;
import cn.partytime.model.AdminTaskModel;
import cn.partytime.common.cachekey.DanmuCacheKey;
import cn.partytime.common.util.ListUtils;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by lENOVO on 2016/10/11.
 */
@Component
public class RealTimeDanmuService {

    private static final Logger logger = LoggerFactory.getLogger(RealTimeDanmuService.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private DanmuChannelRepository danmuChannelRepository;

    @Autowired
    private ManagerCachService managerCachService;


    public void pushDanmuToManager(String partyId) {

        List<Channel> channelList = danmuChannelRepository.findChannelListByPartyId(partyId);
        if (!ListUtils.checkListIsNotNull(channelList)) {
            return;
        }
        Object object = redisService.popFromList(DanmuCacheKey.SEND_DANMU_CACHE_LIST + partyId);
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
        //TODO:弹幕未审核

    }

    public void appointTaskToManager(String message, String partyId, List<Channel> channelList) {
        logger.info("开始给管理员分配任务");
        List<AdminTaskModel> adminTaskModelList = new ArrayList<AdminTaskModel>();
        for (Channel channel : channelList) {
            AdminTaskModel adminTaskModel = danmuChannelRepository.findAdminTaskModel(channel);
            if(adminTaskModel.getCheckFlg()==0){
                String channelId = channel.id().asLongText();
                adminTaskModel.setChannel(channel);
                int count = managerCachService.appointTaskCount(channelId, partyId);
                adminTaskModel.setPartyId(partyId);
                adminTaskModel.setCount(count);
                adminTaskModelList.add(adminTaskModel);
                logger.info("管理员信息:{}", JSON.toJSONString(adminTaskModel));
            }
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
            managerCachService.addAppointCount(adminTaskModel.getAdminId(), adminTaskModel.getPartyId());
        }

    }
}
