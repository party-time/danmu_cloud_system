package cn.partytime.service;

import cn.partytime.config.DanmuChannelRepository;
import cn.partytime.model.DanmuClientModel;
import com.alibaba.fastjson.JSON;
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
        ConcurrentHashMap<Channel,DanmuClientModel> channelConcurrentHashMap = danmuChannelRepository.findConcurrentHashMap();
        if (channelConcurrentHashMap != null && channelConcurrentHashMap.size() > 0) {
            for (ConcurrentHashMap.Entry<Channel, DanmuClientModel> entry : channelConcurrentHashMap.entrySet()) {
                DanmuClientModel danmuClientModel = entry.getValue();
                if(danmuClientModel.getClientType()==clientType){
                    addressSet.add(danmuClientModel.getAddressId());
                }
            }
        }
        return addressSet;
    }

    public int findDanmuClientCountByAddressIdAndClientType(String addressId,int clientType){
        int count = 0;
        ConcurrentHashMap<Channel,DanmuClientModel> channelConcurrentHashMap = danmuChannelRepository.findConcurrentHashMap();
        if (channelConcurrentHashMap != null && channelConcurrentHashMap.size() > 0) {
            for (ConcurrentHashMap.Entry<Channel, DanmuClientModel> entry : channelConcurrentHashMap.entrySet()) {
                DanmuClientModel danmuClientModel = entry.getValue();
                if(addressId.equals(danmuClientModel.getAddressId()) && danmuClientModel.getClientType()==clientType){
                    count++;
                }
            }
        }
        return count;
    }


    public List<String> findScreenAddresIdList(int clientType){
        List<String> addressList = new ArrayList<String>();
        ConcurrentHashMap<Channel,DanmuClientModel> channelConcurrentHashMap = danmuChannelRepository.findConcurrentHashMap();
        if (channelConcurrentHashMap != null && channelConcurrentHashMap.size() > 0) {
            for (ConcurrentHashMap.Entry<Channel, DanmuClientModel> entry : channelConcurrentHashMap.entrySet()) {
                DanmuClientModel danmuClientModel = entry.getValue();
                Channel channel = entry.getKey();
                if(clientType==danmuClientModel.getClientType()){
                    addressList.add(danmuClientModel.getAddressId());
                }
            }
        }
        return addressList;
    }

    public List<Channel> findDanmuClientChannelAddressByClientType(String addressId,int clientType){
        List<Channel> channelList = new ArrayList<Channel>();
        ConcurrentHashMap<Channel,DanmuClientModel> channelConcurrentHashMap = danmuChannelRepository.findConcurrentHashMap();
        if (channelConcurrentHashMap != null && channelConcurrentHashMap.size() > 0) {
            for (ConcurrentHashMap.Entry<Channel, DanmuClientModel> entry : channelConcurrentHashMap.entrySet()) {
                DanmuClientModel danmuClientModel = entry.getValue();
                Channel channel = entry.getKey();
                if(clientType==danmuClientModel.getClientType() && addressId.equals(danmuClientModel.getAddressId())){
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
        ConcurrentHashMap<Channel,DanmuClientModel> channelConcurrentHashMap = danmuChannelRepository.findConcurrentHashMap();
        if (channelConcurrentHashMap != null && channelConcurrentHashMap.size() > 0) {
            for (ConcurrentHashMap.Entry<Channel, DanmuClientModel> entry : channelConcurrentHashMap.entrySet()) {
                DanmuClientModel danmuClientModel = entry.getValue();

                if (code.equals(danmuClientModel.getRegistCode()) && danmuClientModel.getClientType() == clientType) {
                    logger.info("danmuClientModel==========================={}",JSON.toJSONString(danmuClientModel));
                    return entry.getKey();
                }
            }
        }
        return null;
    }

}
