package cn.partytime.collector.service;

import cn.partytime.collector.config.DanmuChannelRepository;
import cn.partytime.collector.model.DanmuClientModel;
import cn.partytime.collector.model.ProtocolModel;
import cn.partytime.common.cachekey.ClientCommandCacheKey;
import cn.partytime.common.constants.ClientConst;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

@Service
public class ClientCommandService {


    @Autowired
    private DanmuChannelRepository danmuChannelRepository;

    @Autowired
    private RedisService redisService;

    public void pubCommandToJavaClient(String addressId){
        ConcurrentHashMap<Channel, DanmuClientModel> danmuClientModelConcurrentHashMap =  danmuChannelRepository.findConcurrentHashMap();
        String key = ClientCommandCacheKey.PUB_ClIENT_COMMAND_CACHE + addressId;
        Object data = redisService.get(key);
        if(data!=null){
            ProtocolModel protocolModel = JSON.parseObject(String.valueOf(data),ProtocolModel.class);

            if (danmuClientModelConcurrentHashMap != null && danmuClientModelConcurrentHashMap.size() > 0) {
                for (ConcurrentHashMap.Entry<Channel, DanmuClientModel> entry : danmuClientModelConcurrentHashMap.entrySet()) {
                    DanmuClientModel danmuClientModel = entry.getValue();
                    if (addressId.equals(danmuClientModel.getAddressId()) && danmuClientModel.getClientType()==Integer.parseInt(ClientConst.CLIENT_TYPE_JAVACLIENT)) {
                        Channel channel = entry.getKey();
                        String message = JSON.toJSONString(protocolModel);
                        channel.writeAndFlush(new TextWebSocketFrame(message));
                    }
                }
            }
        }
    }
}
