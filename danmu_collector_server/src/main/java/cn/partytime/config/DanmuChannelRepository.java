package cn.partytime.config;

import cn.partytime.model.DanmuClientInfoModel;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;


/**
 * 保存所有的频道
 * Created by user on 16/6/24.
 */
@Component
public class DanmuChannelRepository {

    private static final Logger logger = LoggerFactory.getLogger(DanmuChannelRepository.class);

    //通道与客户端关系
    private ConcurrentHashMap<Channel, DanmuClientInfoModel> channelConcurrentHashMap = new ConcurrentHashMap<Channel, DanmuClientInfoModel>();

    public  ConcurrentHashMap<Channel, DanmuClientInfoModel>  findConcurrentHashMap(){
        return channelConcurrentHashMap;
    }

    public void set(Channel channel, DanmuClientInfoModel danmuClientInfoModel) {
        logger.info("新的通道连接");
        channelConcurrentHashMap.put(channel, danmuClientInfoModel);
    }

    public DanmuClientInfoModel get(Channel channel) {
        return  channelConcurrentHashMap.get(channel);
    }

    public void remove(Channel channel) {
        logger.info("移除客户端");
        channelConcurrentHashMap.remove(channel);
    }

    public int size() {
        logger.info("获取客户端数量");
        return channelConcurrentHashMap.size();
    }

    public int findDanmuClientCount(int type,String addressId){

        int count =0;
        for (ConcurrentHashMap.Entry<Channel, DanmuClientInfoModel> entry : channelConcurrentHashMap.entrySet()) {
            DanmuClientInfoModel danmuClientInfoModel = entry.getValue();
            if (addressId.equals(danmuClientInfoModel.getAddressId()) && danmuClientInfoModel.getClientType()==type) {
                count = count+1;
            }
        }
        return count;
    }


}
