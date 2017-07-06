package cn.partytime.check.config;


import cn.partytime.check.model.AdminTaskModel;
import cn.partytime.common.util.ListUtils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 保存所有的频道
 * Created by user on 16/6/24.
 */
@Component
public class DanmuChannelRepository {

    private static final Logger logger = LoggerFactory.getLogger(DanmuChannelRepository.class);

    private  ConcurrentHashMap<Channel, AdminTaskModel> channelConcurrentHashMap = new ConcurrentHashMap<Channel, AdminTaskModel>();
    //屏幕与通道的关系
    private ConcurrentHashMap<String, List<Channel>> managerConcurrentHashMap = new ConcurrentHashMap<String, List<Channel>>();


    public void set(Channel channel, AdminTaskModel adminTaskModel) {
        channelConcurrentHashMap.put(channel, adminTaskModel);
    }

    public AdminTaskModel get(Channel channel) {
        return channelConcurrentHashMap.get(channel);
    }


    public ConcurrentHashMap<Channel, AdminTaskModel> findAdminTaskModelConcurrentHashMap(){
        return channelConcurrentHashMap;
    }


    public void remove(Channel channel) {

        channelConcurrentHashMap.remove(channel);
    }




    public int size() {
        return channelConcurrentHashMap.size();
    }

    /**
     * 保存管理员与活动的关系
     *
     * @param partyId
     * @param channel
     */
    public void setManagerChannelIntoList(String partyId, Channel channel) {

        List<Channel> channelList = managerConcurrentHashMap.get(partyId);
        if (!ListUtils.checkListIsNotNull(channelList)) {
            channelList = new ArrayList<Channel>();
        }
        channelList.add(channel);
        managerConcurrentHashMap.put(partyId, channelList);


    }

    /**
     * 获取本活动下管理员的通道
     *
     * @param partyId
     * @return
     */
    public List<Channel> getManagerChannelList(String partyId) {
        return managerConcurrentHashMap.get(partyId);
    }

    /**
     * 获取活动下所有管理员
     *
     * @return
     */
    public List<String> getManagerAddressList() {
        Iterator<Map.Entry<String, List<Channel>>> it = managerConcurrentHashMap.entrySet().iterator();
        List<String> partyIdList = new ArrayList<String>();
        while (it.hasNext()) {
            Map.Entry<String, List<Channel>> entry = it.next();
            partyIdList.add(entry.getKey());
        }
        return partyIdList;
    }

    /**
     * 移除管理员与活动的关系
     *
     * @param channel
     */
    public void removeManagerChannel(String partyId, Channel channel) {

        List<Channel> channelList = managerConcurrentHashMap.get(partyId);
        if (ListUtils.checkListIsNotNull(channelList) && channelList.contains(channel)) {
            channelList.remove(channel);
            managerConcurrentHashMap.put(partyId, channelList);
        }

    }

}
