package cn.partytime.config;


import cn.partytime.common.constants.PartyConst;
import cn.partytime.model.AdminTaskModel;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
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


    public Channel getChannelByPartyTypeAndAuthKey(int partyType,String adminId){
        ConcurrentHashMap<Channel, AdminTaskModel>  adminTaskModelConcurrentHashMap = new ConcurrentHashMap<Channel, AdminTaskModel>();
        if(partyType==0){
            adminTaskModelConcurrentHashMap = channelPartyConcurrentHashMap;
        }else{
            adminTaskModelConcurrentHashMap = channelFilmConcurrentHashMap;
        }
        Iterator<Map.Entry<Channel, AdminTaskModel>> entries = adminTaskModelConcurrentHashMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Channel, AdminTaskModel> entry = entries.next();
            AdminTaskModel adminTaskModel =  entry.getValue();
            logger.info("adminTaskModel:{}", JSON.toJSONString(adminTaskModel));
            if(adminId.equals(adminTaskModel.getAdminId())){
                return entry.getKey();
            }
        }
        return null;
    }

    public int getAdminCount(int type){
        if(type==0){
            return channelPartyConcurrentHashMap.size();
        }
        return channelFilmConcurrentHashMap.size();
    }

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


    public List<Channel> findAdminTaskModelFilmChannelList(){
        List<Channel> channelList = new ArrayList<>();
        Iterator<Map.Entry<Channel, AdminTaskModel>> entries = channelFilmConcurrentHashMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Channel, AdminTaskModel> entry = entries.next();
            channelList.add(entry.getKey());
        }
        return channelList;
    }

    public List<Channel> findAdminTaskModelChnnelListByPartyId(int partyType,String partyId) {
        ConcurrentHashMap<Channel, AdminTaskModel> adminTaskModelConcurrentHashMap = new ConcurrentHashMap<Channel, AdminTaskModel>();
        if(partyType==PartyConst.PARTY_TYPE_PARTY){
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

    public Channel getChannelByAdminId(String adminId,int partyType){
        ConcurrentHashMap<Channel, AdminTaskModel> adminTaskModelConcurrentHashMap = new ConcurrentHashMap<Channel, AdminTaskModel>();
        if(partyType==PartyConst.PARTY_TYPE_PARTY){
            adminTaskModelConcurrentHashMap = channelPartyConcurrentHashMap;
        }else{
            adminTaskModelConcurrentHashMap = channelFilmConcurrentHashMap;
        }

        Iterator<Map.Entry<Channel, AdminTaskModel>> entries = adminTaskModelConcurrentHashMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Channel, AdminTaskModel> entry = entries.next();
            if(adminId.equals(entry.getValue().getAdminId())){
               return  entry.getKey();
            }
        }
        return null;
    }

    public Set<String> getPartySet(){
        //List<Channel> channelList = new ArrayList<>();
        Set<String> partySet = new HashSet<String>();
        Iterator<Map.Entry<Channel, AdminTaskModel>> entries = channelPartyConcurrentHashMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<Channel, AdminTaskModel> entry = entries.next();
            if(entry.getValue().getPartyType() == 0){
                partySet.add(entry.getValue().getPartyId());
            }
        }
        return partySet;
    }
}
