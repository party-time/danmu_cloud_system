package cn.partytime.service;

import cn.partytime.config.DanmuChannelRepository;
import cn.partytime.model.DanmuClientModel;
import cn.partytime.model.ProtocolModel;
import cn.partytime.common.cachekey.ClientCommandCacheKey;
import cn.partytime.common.constants.ClientConst;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

@Service
@Slf4j
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
            String command  = String.valueOf(data);
            log.info("接收的命令是:{}",command);
            ProtocolModel protocolModel = JSON.parseObject(command,ProtocolModel.class);
            if (danmuClientModelConcurrentHashMap != null && danmuClientModelConcurrentHashMap.size() > 0) {
                log.info("==================================================================================>");
                log.info("size==================================================================================>{}",danmuClientModelConcurrentHashMap.size());
                for (ConcurrentHashMap.Entry<Channel, DanmuClientModel> entry : danmuClientModelConcurrentHashMap.entrySet()) {
                    DanmuClientModel danmuClientModel = entry.getValue();
                    log.info("java clientType:{}",danmuClientModel.getClientType());
                    if (addressId.equals(danmuClientModel.getAddressId()) && danmuClientModel.getClientType()==Integer.parseInt(ClientConst.CLIENT_TYPE_JAVACLIENT)) {
                        Channel channel = entry.getKey();
                        String message = JSON.toJSONString(protocolModel);
                        log.info("向地址:{}Javaclient发送命令:{}",addressId,message);
                        channel.writeAndFlush(new TextWebSocketFrame(message));
                    }
                }
            }
        }
    }
}
