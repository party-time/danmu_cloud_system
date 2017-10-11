package cn.partytime.cache.projector;

import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.constants.LogCodeConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectorAlarmCacheService {


    @Autowired
    private AlarmCacheService alarmCacheService;


    /**
     * 电影开始或者结束
     * @param addressId
     */
    public void removeAlarmAllCache(String addressId,String regiterCode){
        String typeArray[]={
                AlarmKeyConst.ALARM_KEY_PROJECTORNOTCLOSE,
                AlarmKeyConst.ALARM_KEY_PROJECTORNOTOPEN
        };
        for(int i=0; i<typeArray.length; i++) {
            alarmCacheService.removeAlarmCount(addressId, typeArray[i],regiterCode);
        }
    }

}
