package cn.partytime.service.client;

import cn.partytime.business.command.ControlCommandService;
import cn.partytime.common.cachekey.client.ClientCommandCacheKey;
import cn.partytime.common.constants.ProtocolConst;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/1 0001.
 */

@Service
@Slf4j
public class ClientService {


    @Autowired
    private ControlCommandService controlCommandService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisService redisService;


    public void sendCommand(String command,String addressId,String callback){
        /*String key = ClientCommandCacheKey.PUB_ClIENT_COMMAND_CACHE + addressId;


        Map<String,Object> commandMap = new HashMap<String,Object>();

        commandMap.put("type", ProtocolConst.PROTOCOL_CLIENT_COMMAND);

        Map<String,Object> dataMap = new HashMap<String,Object>();

        dataMap.put("bcallBack",callback);
        dataMap.put("name",command);
        commandMap.put("data",dataMap);

        String message = JSON.toJSONString(commandMap);
        log.info("发送给地址:{}客户端的指令{}",addressId,message);
        redisService.set(key, message);

        redisService.expire(key, 60);*/

        Map<String,Object> dataMap = new HashMap<String,Object>();

        dataMap.put("bcallBack",callback);
        dataMap.put("name",command);
        controlCommandService.sendClientCommand(addressId,dataMap,ClientCommandCacheKey.PUB_ClIENT_COMMAND_CACHE);

        //redisService.subPub("client:command", addressId);
        //redisTemplate.convertAndSend("client:command", addressId);
    }


}
