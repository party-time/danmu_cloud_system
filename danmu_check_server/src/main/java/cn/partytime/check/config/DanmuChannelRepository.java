package cn.partytime.check.config;


import cn.partytime.check.model.AdminTaskModel;
import cn.partytime.common.util.ListUtils;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.plaf.PanelUI;
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


    private  ConcurrentHashMap<Channel, AdminTaskModel> channelPartyConcurrentHashMap = new ConcurrentHashMap<Channel, AdminTaskModel>();

    private  ConcurrentHashMap<Channel, AdminTaskModel> channelFilmConcurrentHashMap = new ConcurrentHashMap<Channel, AdminTaskModel>();

    /**
     * 保存通道与管理员的关系
     * @param partyTpe
     * @param channel
     * @param adminTaskModel
     */
    public void saveChannelAdminRelation(int partyTpe,Channel channel,AdminTaskModel adminTaskModel){
        //活动编号
        if(partyTpe==0){
            channelPartyConcurrentHashMap.put(channel,adminTaskModel);
        }else{
            channelFilmConcurrentHashMap.put(channel,adminTaskModel);
        }
    }


    public List<Channel> findChannelListByPartyId(String partyId){
        return findChannelbyMapAndPartyId(channelPartyConcurrentHashMap,partyId);
    }






    public List<Channel> findAdminTaskModelChnnelListByPartyId(int partyType,String partyId) {
        ConcurrentHashMap<Channel, AdminTaskModel> adminTaskModelConcurrentHashMap = new ConcurrentHashMap<Channel, AdminTaskModel>();
        if(partyType==0){
            adminTaskModelConcurrentHashMap = channelPartyConcurrentHashMap;
        }else{
            adminTaskModelConcurrentHashMap = channelFilmConcurrentHashMap;
        }

        return findChannelbyMapAndPartyId(adminTaskModelConcurrentHashMap,partyId);
    }

    public List<Channel> findChannelbyMapAndPartyId(ConcurrentHashMap<Channel, AdminTaskModel> map ,String partyId){
        List<Channel> channelList = new ArrayList<>();
        Iterator<Map.Entry<Channel, AdminTaskModel>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Channel, AdminTaskModel> entry = entries.next();
            if(partyId.equals(entry.getValue().getPartyId())){
                channelList.add(entry.getKey());
            }
        }
        return channelList;
    }




    public AdminTaskModel  findAdminTaskModel(int partyType,Channel channel) {
        if(partyType==0){
            return channelPartyConcurrentHashMap.get(channel);
        }else{
            return channelFilmConcurrentHashMap.get(channel);
        }
    }

    public AdminTaskModel  findAdminTaskModel(Channel channel) {
        AdminTaskModel adminTaskModel = channelPartyConcurrentHashMap.get(channel);

        if(adminTaskModel==null){
            adminTaskModel = channelFilmConcurrentHashMap.get(channel);
        }
        return adminTaskModel;
    }

    public void remove(Channel channel){
        channelPartyConcurrentHashMap.remove(channel);
        channelFilmConcurrentHashMap.remove(channel);
    }


}
