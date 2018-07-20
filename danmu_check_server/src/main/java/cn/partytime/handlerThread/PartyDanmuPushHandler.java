package cn.partytime.handlerThread;


import cn.partytime.cache.danmu.DanmuCacheService;
import cn.partytime.common.util.ListUtils;
import cn.partytime.config.DanmuChannelRepository;
import cn.partytime.dataRpc.RpcDanmuService;
import cn.partytime.model.AdminTaskModel;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PartyDanmuPushHandler {

    @Autowired
    private DanmuChannelRepository danmuChannelRepository;

    @Autowired
    private DanmuCacheService danmuCacheService;

    @Autowired
    private RpcDanmuService rpcDanmuService;


    public void pushDanmuToManager(Object object, List<Channel> channelList,String partyId) {
        String acceptMessage = String.valueOf(object);
        log.info("推送给管理员的消息:{}", acceptMessage);
        Map<String, Object> danmuMap = (Map<String, Object>) JSON.parse(acceptMessage);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", "normalDanmu");
        map.put("data", danmuMap);
        String danmuLogId = String.valueOf(danmuMap.get("id"));

        rpcDanmuService.setAdminAccepetTime(danmuLogId);

        pushDanmu(channelList,partyId,danmuLogId,map,null);
    }


    public void pushOfflineAdminDanmuToOtherAdmin(String adminId,String partyId,String danmuLogId,Map<String, Object> map){
        List<Channel> channelList = danmuChannelRepository.findChannelListByPartyId(partyId);
        pushDanmu(channelList,partyId,danmuLogId,map,adminId);
    }

    public void pushDanmu(List<Channel> channelList,String partyId,String danmuLogId,Map<String, Object> map,String srcAdmin){
        log.info("开始给管理员分配任务");
        List<AdminTaskModel> adminTaskModelList = new ArrayList<AdminTaskModel>();
        for (Channel channel : channelList) {
            AdminTaskModel adminTaskModel = danmuChannelRepository.findAdminTaskModel(channel);
            if (adminTaskModel.getCheckFlg() == 0) {
                adminTaskModel.setChannel(channel);
                adminTaskModelList.add(adminTaskModel);
            }
        }
        if (ListUtils.checkListIsNotNull(adminTaskModelList)) {

            int random = (int) (Math.random() * adminTaskModelList.size());
            log.info("给{}分配弹幕", random);
            AdminTaskModel adminTaskModel = adminTaskModelList.get(random);
            //分配弹幕给管理员
            danmuCacheService.addPartyDanmuToCheckUserSortSet(partyId,adminTaskModel.getAdminId(),danmuLogId);

            danmuCacheService.setSendDanmuInfo(danmuLogId,map);

            if(!StringUtils.isEmpty(srcAdmin)){
                //从自己的弹幕队列中清除弹幕
                danmuCacheService.reomvePartyDanmuFromCheckUserSortSet(partyId,srcAdmin,danmuLogId);
            }


            Channel channel = adminTaskModel.getChannel();
            channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(map)));
        }
    }



}
