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
import java.util.List;

@Slf4j
@Service
public class FilmDanmuHandler {

    @Autowired
    private DanmuChannelRepository danmuChannelRepository;


    public void pushDanmuToManager(Object object, List<Channel> channelList) {
        log.info("开始给管理员分配任务");
        List<AdminTaskModel> adminTaskModelList = new ArrayList<AdminTaskModel>();
        for (Channel channel : channelList) {
            AdminTaskModel adminTaskModel = danmuChannelRepository.findAdminTaskModel(channel);
            if(adminTaskModel.getCheckFlg()==0){
                adminTaskModel.setChannel(channel);
                adminTaskModelList.add(adminTaskModel);
                //log.info("管理员信息:{}", JSON.toJSONString(adminTaskModel));
            }
        }

        if (ListUtils.checkListIsNotNull(adminTaskModelList)) {
            /*Collections.sort(adminTaskModelList, new Comparator<AdminTaskModel>() {
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
            AdminTaskModel adminTaskModel = adminTaskModelList.get(0);*/
            int random = (int) (Math.random() * adminTaskModelList.size());
            log.info("给{}分配弹幕",random);
            AdminTaskModel adminTaskModel = adminTaskModelList.get(random);

            Channel channel = adminTaskModel.getChannel();
            channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(object)));
            //managerCachService.addAppointCount(adminTaskModel.getAdminId());
        }

    }
}
