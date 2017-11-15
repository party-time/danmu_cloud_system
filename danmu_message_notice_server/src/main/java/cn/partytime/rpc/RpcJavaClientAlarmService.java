package cn.partytime.rpc;


import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.proxy.MessageHandlerService;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.model.PartyModel;
import cn.partytime.service.DanmuAlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rpcJavaClientAlarm")
@Slf4j
public class RpcJavaClientAlarmService {

    @Autowired
    private DanmuAlarmService danmuAlarmService;

    @Autowired
    private MessageHandlerService messageHandlerService;


    @Autowired
    private AlarmCacheService alarmCacheService;

    @Autowired
    private RpcDanmuAddressService rpcDanmuAddressService;

    @RequestMapping(value = "/javaClientException" ,method = RequestMethod.GET)
    public void javaClientAlarm(@RequestParam String type, @RequestParam Integer number, @RequestParam String addressId) {
        log.info("--------------java Client run Exception-------------------");

        Map<String,String> map = new HashMap<>();
        DanmuAddressModel danmuAddressModel = rpcDanmuAddressService.findById(addressId);
        map.put("addressName",danmuAddressModel.getName());
        sendMessage(LogCodeConst.DeviceLogCode.JAVACLIENT_EXECUTESHELL_EXCEPTION,map,number,AlarmKeyConst.JAVACLIENTSHELLERROR);
    }

    private void sendMessage(String type,Map<String,String> map,int number,String typeName){
        if(map!=null){
            String addressId = map.get("addressId");
            if(number==1){
                map.put("screen","左边屏幕");
            }else{
                map.put("screen","右边屏幕");
            }
            int cacheCount = alarmCacheService.findAlarmCount(addressId,typeName);
            if(cacheCount>0){
                log.info("Java客户端执行脚本异常{}告警发出的次数超过上限",typeName);
                return;
            }
            alarmCacheService.addAlarmCount(60*60*5,addressId,typeName,String.valueOf(number));
            MessageObject<Map<String,String>> mapMessageObject = new MessageObject<Map<String,String>>(type,map);
            mapMessageObject.setValue(0);
            mapMessageObject.setThreshold(0);
            messageHandlerService.messageHandler(danmuAlarmService,mapMessageObject);
        }
    }

}
