package cn.partytime.cache.collector;

import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.cache.common.CommonCacheService;
import cn.partytime.common.cachekey.collector.CollectorCacheKey;
import cn.partytime.common.constants.AlarmKeyConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CollectorAlarmCacheService {

    @Autowired
    private AlarmCacheService alarmCacheService;

    @Autowired
    private CommonCacheService commonCacheService;

    /**
     * 管理员重新登录
     */
    public void removeAlarmAllCache(String addressId){
        String typeArray[]={
                AlarmKeyConst.ALARM_KEY_NETWORKERROR
        };
        //清除告警次数
        for(int i=0; i<typeArray.length; i++) {
            //CollectorCacheKey.BASE_ALARM_KEY,addressId
            alarmCacheService.removeAlarmCount(CollectorCacheKey.BASE_ALARM_KEY, typeArray[i],addressId);
        }
        //清除掉线时间
        commonCacheService.removeCache( CollectorCacheKey.COLLECTOR_FLASH_CLIENT_OFFLINE_TIME+addressId);
    }
}
