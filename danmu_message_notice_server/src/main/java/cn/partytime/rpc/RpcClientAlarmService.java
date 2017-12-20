package cn.partytime.rpc;

import cn.partytime.business.command.ControlCommandService;
import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.cache.collector.CollectorCacheService;
import cn.partytime.common.cachekey.client.ClientCommandCacheKey;
import cn.partytime.common.cachekey.collector.CollectorCacheKey;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.common.constants.ProtocolConst;
import cn.partytime.common.util.ListUtils;
import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.dataRpc.RpcDanmuClientService;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.proxy.MessageHandlerService;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.model.DanmuClientModel;
import cn.partytime.redis.service.RedisService;
import cn.partytime.service.DanmuAlarmService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dm on 2017/7/19.
 */

@RestController
@RequestMapping("/rpcClient")
@Slf4j
public class RpcClientAlarmService {

    @Autowired
    private AlarmCacheService alarmCacheService;

    @Autowired
    private RpcDanmuAddressService rpcDanmuAddressService;

    @Autowired
    private DanmuAlarmService danmuAlarmService;

    @Autowired
    private MessageHandlerService messageHandlerService;

    @Autowired
    private CollectorCacheService collectorCacheService;

    @Autowired
    private RpcDanmuClientService danmuClientService;


    @Autowired
    private RedisService redisService;


    @Autowired
    private ControlCommandService controlCommandService;

    @RequestMapping(value = "/clientNetError" ,method = RequestMethod.GET)
    public void clientNetError(@RequestParam String addressId) {


        int cacheCount = alarmCacheService.findAlarmCount(CollectorCacheKey.BASE_ALARM_KEY,AlarmKeyConst.ALARM_KEY_NETWORKERROR,addressId);
        if(cacheCount>0){
            log.info("客户端不在线的告警超过上限");
            return;
        }

        //发送重启指令

        log.info("-------------客户端网络异常，发重启指令---------------------");
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("bcallBack",null);
        dataMap.put("name","appRestart");
        controlCommandService.sendClientCommand(addressId,dataMap,ClientCommandCacheKey.PUB_ClIENT_COMMAND_CACHE);
        /*Map<String,Object> commandMap = new HashMap<>();
        commandMap.put("type", ProtocolConst.PROTOCOL_CLIENT_COMMAND);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("bcallBack",null);
        dataMap.put("name","appRestart");
        commandMap.put("data",dataMap);
        //rpcClientService.sendCommandToClinet(addressId,commandMap);
        String key = ClientCommandCacheKey.PUB_ClIENT_COMMAND_CACHE + addressId;
        String message = JSON.toJSONString(commandMap);
        redisService.set(key, message);
        redisService.expire(key, 60);
        redisService.subPub("client:command",addressId);*/


        alarmCacheService.addAlarmCount(0,CollectorCacheKey.BASE_ALARM_KEY,AlarmKeyConst.ALARM_KEY_NETWORKERROR,addressId);
        log.info("地址:{} 客户端告警",addressId);

        DanmuAddressModel danmuAddressModel = rpcDanmuAddressService.findById(addressId);
        log.info("地址信息：{}", JSON.toJSONString(danmuAddressModel));
        Map<String,String> map = new HashMap<String,String>();
        map.put("key", AlarmKeyConst.ALARM_KEY_NETWORKERROR);
        map.put("addressId", addressId);
        map.put("addressName", danmuAddressModel.getName());
        List<String> registerCodeList =  collectorCacheService.findFlahOfflineCLientList(addressId);
        if(ListUtils.checkListIsNotNull(registerCodeList)){
            for(int i=0; i<registerCodeList.size(); i++){
                DanmuClientModel danmuClient = danmuClientService.findByRegistCode(registerCodeList.get(i));
                map.put("screen",danmuClient.getName());
                sendMessage(LogCodeConst.CLientLogCode.FLASH_NETWORK_EXCEPTION,map);
            }
        }
    }

    private void sendMessage(String type,Map<String,String> map){
        MessageObject<Map<String,String>> mapMessageObject = new MessageObject<Map<String,String>>(type,map);
        mapMessageObject.setValue(0);
        mapMessageObject.setThreshold(0);
        messageHandlerService.messageHandler(danmuAlarmService,mapMessageObject);
    }
}
