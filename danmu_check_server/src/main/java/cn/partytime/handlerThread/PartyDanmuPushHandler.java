package cn.partytime.handlerThread;


import cn.partytime.common.util.ListUtils;
import cn.partytime.config.DanmuChannelRepository;
import cn.partytime.model.AdminTaskModel;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PartyDanmuPushHandler {

    @Autowired
    private DanmuChannelRepository danmuChannelRepository;

    public void pushDanmuToManager(Object object, List<Channel> channelList) {
        String acceptMessage = String.valueOf(object);
        log.info("推送给管理员的消息:{}", acceptMessage);
        Map<String, Object> danmuMap = (Map<String, Object>) JSON.parse(acceptMessage);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", "normalDanmu");
        map.put("data", danmuMap);
        String msg = JSON.toJSONString(map);

        log.info("开始给管理员分配任务");
        List<AdminTaskModel> adminTaskModelList = new ArrayList<AdminTaskModel>();
        for (Channel channel : channelList) {
            AdminTaskModel adminTaskModel = danmuChannelRepository.findAdminTaskModel(channel);
            if (adminTaskModel.getCheckFlg() == 0) {
                adminTaskModel.setChannel(channel);
                adminTaskModelList.add(adminTaskModel);

                //log.info("管理员信息:{}", JSON.toJSONString(adminTaskModel));
            }
        }

        if (ListUtils.checkListIsNotNull(adminTaskModelList)) {

            int random = (int) (Math.random() * adminTaskModelList.size());
            log.info("给{}分配弹幕", random);
            AdminTaskModel adminTaskModel = adminTaskModelList.get(random);

            Channel channel = adminTaskModel.getChannel();
            channel.writeAndFlush(new TextWebSocketFrame(msg));
            //managerCachService.addAppointCount(adminTaskModel.getAdminId(), adminTaskModel.getPartyId());
        }

    }
}
