package cn.partytime.rpc;

import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.common.cachekey.AdminUserCacheKey;
import cn.partytime.common.cachekey.collector.CollectorCacheKey;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.proxy.MessageHandlerService;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.service.ClientServiceAlarmService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dm on 2017/7/19.
 */

@RestController
@RequestMapping("/rpcClient")
@Slf4j
public class RpcClientAlarmService {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientAlarmService.class);

    @Autowired
    private AlarmCacheService alarmCacheService;

    @Autowired
    private RpcDanmuAddressService rpcDanmuAddressService;

    @Autowired
    private ClientServiceAlarmService clientServiceAlarmService;

    @Autowired
    private MessageHandlerService messageHandlerService;

    @RequestMapping(value = "/clientNetError" ,method = RequestMethod.GET)
    public void clientNetError(@RequestParam String addressId) {

        int cacheCount = alarmCacheService.findAlarmCount(CollectorCacheKey.BASE_ALARM_KEY,LogCodeConst.CLientLogCode.FLASH_NETWORK_EXCEPTION,addressId);
        if(cacheCount>0){
            log.info("客户端不在线的告警超过上限");
            return;
        }
        alarmCacheService.addAlarmCount(0,CollectorCacheKey.BASE_ALARM_KEY,LogCodeConst.CLientLogCode.FLASH_NETWORK_EXCEPTION,addressId);

        DanmuAddressModel danmuAddressModel = rpcDanmuAddressService.findById(addressId);
        Map<String,String> map = new HashMap<String,String>();
        map.put("key", AlarmKeyConst.ALARM_KEY_NETWORKERROR);
        map.put("addressId", addressId);
        map.put("addressName", danmuAddressModel.getAddress());
        sendMessage(LogCodeConst.CLientLogCode.FLASH_NETWORK_EXCEPTION,map);
    }

    private void sendMessage(String type,Map<String,String> map){
        MessageObject<Map<String,String>> mapMessageObject = new MessageObject<Map<String,String>>(type,map);
        mapMessageObject.setValue(0);
        mapMessageObject.setThreshold(0);
        messageHandlerService.messageHandler(clientServiceAlarmService,mapMessageObject);
    }
}
