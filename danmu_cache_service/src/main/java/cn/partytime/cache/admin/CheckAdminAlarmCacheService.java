package cn.partytime.cache.admin;


import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.cache.common.CommonCacheService;
import cn.partytime.common.cachekey.AdminUserCacheKey;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CheckAdminAlarmCacheService {



    @Autowired
    private AlarmCacheService alarmCacheService;

    @Autowired
    private CommonCacheService commonCacheService;

    /**
     * 管理员重新登录
     */
    public void removeAlarmAllCache(int type){
        String typeArray[]={
                AlarmKeyConst.ALARM_KEY_AUDITOROFFLINE
        };
        //清除告警次数
        for(int i=0; i<typeArray.length; i++) {
            alarmCacheService.removeAlarmCount(AdminUserCacheKey.CHECK_AMDIN_CACHE_KEY, typeArray[i]);
        }
        //清除掉线时间
        commonCacheService.removeCache(AdminUserCacheKey.CHECK_AMIN_OFFLINE_TIME+type);
    }
}
