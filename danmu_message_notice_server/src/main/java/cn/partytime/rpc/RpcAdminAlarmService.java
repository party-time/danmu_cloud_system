package cn.partytime.rpc;

import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.common.cachekey.AdminUserCacheKey;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.service.AdminIsOnLineAlarmService;
import cn.partytime.message.proxy.MessageHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/rpcAdmin")
@Slf4j
public class RpcAdminAlarmService {

    @Autowired
    private AdminIsOnLineAlarmService adminIsOnLineAlarmService;


    @Autowired
    private MessageHandlerService messageHandlerService;

    @Autowired
    private AlarmCacheService alarmCacheService;

    @Value("${alarm.admin.offlineTime}")
    private int adminOfflineTime;



    @RequestMapping(value = "/admiOffLine" ,method = RequestMethod.GET)
    public void admiOffLineAlarm(@RequestParam long time) {
        Date date  = DateUtils.getCurrentDate();
        long subTime = date.getTime() - time;
        long minute = subTime/1000/60;
        if(minute<=adminOfflineTime){
            log.info("当前时间与离线时间小于{}分钟",adminOfflineTime);
            return;
        }
        Map<String,String> map = new HashMap<String,String>();
        map.put("key", AlarmKeyConst.ALARM_KEY_AUDITOROFFLINE);
        sendMessage(LogCodeConst.AdminLogCode.ADMIN_ONLINE_COUNT_ZERO,map,AlarmKeyConst.ALARM_KEY_AUDITOROFFLINE);

        //Map<String,Object> map = new HashMap<>();
        //map.put("key",AlarmKeyConst.ALARM_KEY_AUDITOROFFLINE);
        //MessageObject<Map<String,Object>> mapMessageObject = new MessageObject<Map<String,Object>>(LogCodeConst.AdminLogCode.ADMIN_ONLINE_COUNT_ZERO,map);
        //messageHandlerService.messageHandler(adminIsOnLineAlarmService,mapMessageObject);
    }


    private void sendMessage(String type,Map<String,String> map,String typeName){
        if(map!=null){
            int cacheCount = alarmCacheService.findAlarmCount(AdminUserCacheKey.CHECK_AMDIN_CACHE_KEY, typeName);
            if(cacheCount>0){
                log.info("管理员不在线告警发出的次数超过上限");
                return;
            }
            alarmCacheService.addAlarmCount(0,AdminUserCacheKey.CHECK_AMDIN_CACHE_KEY,typeName);
            MessageObject<Map<String,String>>  mapMessageObject = new MessageObject<Map<String,String>>(type,map);
            mapMessageObject.setValue(0);
            mapMessageObject.setThreshold(0);
            messageHandlerService.messageHandler(adminIsOnLineAlarmService,mapMessageObject);
        }
    }

}
