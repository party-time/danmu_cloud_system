package cn.partytime.cache.party;


import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.common.cachekey.alarm.AlarmCacheKey;
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


    /**
     * 电影开始或者结束
     * @param addressId
     */
    public void removeAlarmAllCache(String addressId){
        String typeArray[]={
                LogCodeConst.PartyLogCode.MOVIE_TIME_TOO_LONG,
                LogCodeConst.PartyLogCode.MOVIE_TIME_TOO_SHORT
        };
        for(int i=0; i<typeArray.length; i++) {
            alarmCacheService.removeAlarmCount(addressId, typeArray[i]);
        }
    }


}
