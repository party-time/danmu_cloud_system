package cn.partytime.cache.danmu;

import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.common.cachekey.DanmuCacheKey;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class DanmuAlarmCacheService {

    @Autowired
    private AlarmCacheService alarmCacheService;

    @Autowired
    private RedisService redisService;

    public void addTimerDanmuNotPlayResource(String addressId,String idd,long time){
        String key = DanmuCacheKey.TIMERDANMU_NOT_PLARY_RESOUR_LIST+addressId;
        redisService.setSortSet(key,0,idd);
        redisService.expire(key,time);
    }

    public boolean timerDanmuNotPlayResourceisExist(String addressId,String idd){
        String key = DanmuCacheKey.TIMERDANMU_NOT_PLARY_RESOUR_LIST+addressId;
        Set<String> stringSet = redisService.getSortSetByRnage(key,0,-1,true);
         if(stringSet!=null && stringSet.size()>0){
             for(String str:stringSet){
                 if(idd.equals(str)){
                     return true;
                 }
             }
         }
         return false;
    }

    public void removeDanmuNotPlayResource(String addressId){
        String key = DanmuCacheKey.TIMERDANMU_NOT_PLARY_RESOUR_LIST+addressId;
        redisService.expire(key,0);
    }

    /**
     * 电影开始或者结束
     * @param addressId
     */
    public void removeDanmuAlarmAllCache(String addressId){
        String typeArray[]={
                AlarmKeyConst.ALARM_KEY_PREDANMU,
                AlarmKeyConst.ALARM_KEY_SYSTEMERROR,
                AlarmKeyConst.ALARM_KEY_HISTORYDANMU,
                AlarmKeyConst.ALARM_KEY_TIMERDANMU,
                AlarmKeyConst.ALARM_KEY_DANMUEXCESS
        };
        for(int i=0; i<typeArray.length; i++) {
            alarmCacheService.removeAlarmCount(addressId, typeArray[i]);
        }
        String timerTypeArray[] = {
                AlarmKeyConst.ALARM_KEY_DANMUEXCESS,
                AlarmKeyConst.ALARM_KEY_SYSTEMERROR
        };
        for(int i=0; i<timerTypeArray.length; i++) {
            alarmCacheService.removeAlarmTime(addressId, timerTypeArray[i]);
        }
        removeDanmuNotPlayResource(addressId);


    }

    public void removeDanmuAlarmCacheIfDanmuIsMore(int danmuCount,String addressId){
        if(danmuCount<=15){
            //清除弹幕过量的缓存 和 和时间
            alarmCacheService.removeAlarmCount(addressId, LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISMORE);
            alarmCacheService.removeAlarmTime(addressId, LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISMORE);
        }
        //清除没有弹幕的计数 和 时间
        alarmCacheService.removeAlarmCount(addressId,LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISNULL);
        alarmCacheService.removeAlarmTime(addressId, LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISNULL);
    }
}
