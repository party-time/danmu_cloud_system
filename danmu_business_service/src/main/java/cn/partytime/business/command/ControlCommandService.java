package cn.partytime.business.command;

import cn.partytime.common.cachekey.client.ClientCommandCacheKey;
import cn.partytime.common.constants.ProtocolConst;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ControlCommandService {

    @Autowired
    private RedisService redisService;

    public void sendClientCommand(String addressId,Map<String,Object> dataMap,String cacheKeyFix){
        Map<String,Object> commandObject = new HashMap<String,Object>();
        String key = cacheKeyFix+addressId;
        commandObject.put("type", ProtocolConst.PROTOCOL_CLIENT_COMMAND);
        commandObject.put("data",dataMap);
        String message = JSON.toJSONString(commandObject);
        log.info("发送给服务器的客户端{}", message);
        redisService.set(key, message);
        redisService.expire(key, 60 );
        //通知客户端
        redisService.subPub("client:command", addressId);

    }

    public void sendCommandToJavaClient(String command,String addressId,String callback){
        String key = ClientCommandCacheKey.PUB_ClIENT_COMMAND_CACHE + addressId;
        Map<String,Object> commandMap = new HashMap<String,Object>();
        commandMap.put("type", ProtocolConst.PROTOCOL_CLIENT_COMMAND);
        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("bcallBack",callback);
        dataMap.put("name",command);
        commandMap.put("data",dataMap);
        String message = JSON.toJSONString(commandMap);
        log.info("发送给地址:{}客户端的指令{}",addressId,message);
        redisService.set(key, message);
        redisService.expire(key, 60);

        redisService.subPub("client:command",addressId);
    }
}
