package cn.partytime.cache.party;


import cn.partytime.cache.admin.CheckAdminAlarmCacheService;
import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.cache.collector.CollectorAlarmCacheService;
import cn.partytime.cache.danmu.DanmuAlarmCacheService;
import cn.partytime.common.cachekey.alarm.AlarmCacheKey;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.CommonConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartyAlarmCacheService {

    @Autowired
    private AlarmCacheService alarmCacheService;

    @Autowired
    private CheckAdminAlarmCacheService checkAdminAlarmCacheService;

    @Autowired
    private DanmuAlarmCacheService danmuAlarmCacheService;

    @Autowired
    private CollectorAlarmCacheService collectorAlarmCacheService;

    /**
     * 电影开始或者结束
     * @param addressId
     */
    public void removeAlarmAllCache(String addressId,int type){

        //清除电影时间过长或者过短的告警
        String typeArray[]={
                AlarmKeyConst.ALARM_KEY_MOVIESHORT,
                AlarmKeyConst.ALARM_KEY_MOVIEOVERTIME,
                AlarmKeyConst.ALARM_MOVIESTARTERROR
        };
        for(int i=0; i<typeArray.length; i++) {
            alarmCacheService.removeAlarmCount(addressId, typeArray[i]);
        }
        //清除弹幕相关的所有告警
        danmuAlarmCacheService.removeDanmuAlarmAllCache(addressId);

        //清除管理员相关的所有告警
        checkAdminAlarmCacheService.removeAlarmAllCache(type);

        //清除客户端相关的告警
        collectorAlarmCacheService.removeAlarmAllCache(addressId);
    }


}
