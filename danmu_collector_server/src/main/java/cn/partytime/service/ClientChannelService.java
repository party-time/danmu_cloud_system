package cn.partytime.service;

import cn.partytime.config.DanmuChannelRepository;
import cn.partytime.model.DanmuClientInfoModel;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

@Service
public class ClientChannelService {

    private static final Logger logger = LoggerFactory.getLogger(DanmuChannelRepository.class);

    @Autowired
    private DanmuChannelRepository danmuChannelRepository;

    public Set<String> findClientAddressSet(int clientType){
        Set<String> addressSet = new HashSet<String>();
        ConcurrentHashMap<Channel,DanmuClientInfoModel> channelConcurrentHashMap = danmuChannelRepository.findConcurrentHashMap();
        if (channelConcurrentHashMap != null && channelConcurrentHashMap.size() > 0) {
            for (ConcurrentHashMap.Entry<Channel, DanmuClientInfoModel> entry : channelConcurrentHashMap.entrySet()) {
                DanmuClientInfoModel danmuClientInfoModel = entry.getValue();
                if(danmuClientInfoModel.getClientType()==clientType){
                    addressSet.add(danmuClientInfoModel.getAddressId());
                }
            }
        }
        return addressSet;
    }

    public int findDanmuClientCountByAddressIdAndClientType(String addressId,int clientType){
        int count = 0;
        ConcurrentHashMap<Channel,DanmuClientInfoModel> channelConcurrentHashMap = danmuChannelRepository.findConcurrentHashMap();
        if (channelConcurrentHashMap != null && channelConcurrentHashMap.size() > 0) {
            for (ConcurrentHashMap.Entry<Channel, DanmuClientInfoModel> entry : channelConcurrentHashMap.entrySet()) {
                DanmuClientInfoModel danmuClientInfoModel = entry.getValue();
                if(addressId.equals(danmuClientInfoModel.getAddressId()) && danmuClientInfoModel.getClientType()==clientType){
                    count++;
                }
            }
        }
        return count;
    }


    public Set<String> findScreenAddresIdList(int clientType){
        Set<String> addressSet = new HashSet<String>();
        ConcurrentHashMap<Channel,DanmuClientInfoModel> channelConcurrentHashMap = danmuChannelRepository.findConcurrentHashMap();
        if (channelConcurrentHashMap != null && channelConcurrentHashMap.size() > 0) {
            for (ConcurrentHashMap.Entry<Channel, DanmuClientInfoModel> entry : channelConcurrentHashMap.entrySet()) {
                DanmuClientInfoModel danmuClientInfoModel = entry.getValue();
                Channel channel = entry.getKey();
                if(clientType== danmuClientInfoModel.getClientType()){
                    addressSet.add(danmuClientInfoModel.getAddressId());
                }
            }
        }
        return addressSet;
    }

    public List<Channel> findDanmuClientChannelAddressByClientType(String addressId,int clientType){
        List<Channel> channelList = new ArrayList<Channel>();
        ConcurrentHashMap<Channel,DanmuClientInfoModel> channelConcurrentHashMap = danmuChannelRepository.findConcurrentHashMap();
        if (channelConcurrentHashMap != null && channelConcurrentHashMap.size() > 0) {
            for (ConcurrentHashMap.Entry<Channel, DanmuClientInfoModel> entry : channelConcurrentHashMap.entrySet()) {
                DanmuClientInfoModel danmuClientInfoModel = entry.getValue();
                Channel channel = entry.getKey();
                if(clientType== danmuClientInfoModel.getClientType() && addressId.equals(danmuClientInfoModel.getAddressId())){
                    channelList.add(channel);
                }
            }
        }
        return channelList;
    }

    /**
     * 通过唯一标识获取通道
     *
     * @param code
     * @return
     */
    public Channel findChannelByCode(String code,int clientType) {
        logger.info("获取通道信息");
        ConcurrentHashMap<Channel,DanmuClientInfoModel> channelConcurrentHashMap = danmuChannelRepository.findConcurrentHashMap();
        if (channelConcurrentHashMap != null && channelConcurrentHashMap.size() > 0) {
            for (ConcurrentHashMap.Entry<Channel, DanmuClientInfoModel> entry : channelConcurrentHashMap.entrySet()) {
                DanmuClientInfoModel danmuClientInfoModel = entry.getValue();

                if (code.equals(danmuClientInfoModel.getRegistCode()) && danmuClientInfoModel.getClientType() == clientType) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }


    public List<DanmuClientInfoModel> findDanmuClientModelListByAddressIdAndClientType(String addressId, int clientType){
        List<DanmuClientInfoModel> danmuClientInfoModelList = new ArrayList<DanmuClientInfoModel>();
        ConcurrentHashMap<Channel,DanmuClientInfoModel> channelConcurrentHashMap = danmuChannelRepository.findConcurrentHashMap();
        if (channelConcurrentHashMap != null && channelConcurrentHashMap.size() > 0) {
            for (ConcurrentHashMap.Entry<Channel, DanmuClientInfoModel> entry : channelConcurrentHashMap.entrySet()) {
                DanmuClientInfoModel danmuClientInfoModel = entry.getValue();
                Channel channel = entry.getKey();
                if(clientType== danmuClientInfoModel.getClientType() && addressId.equals(danmuClientInfoModel.getAddressId())){
                    danmuClientInfoModelList.add(danmuClientInfoModel);
                }
            }
        }
        return danmuClientInfoModelList;
    }

}
