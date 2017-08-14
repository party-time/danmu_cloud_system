package cn.partytime.cache.danmu;

import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.common.constants.LogCodeConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DanmuAlarmCacheService {

    @Autowired
    private AlarmCacheService alarmCacheService;

    /**
     * 电影开始或者结束
     * @param addressId
     */
    public void removeDanmuAlarmAllCache(String addressId){
        String typeArray[]={
                LogCodeConst.DanmuLogCode.PREDANMU_ISNULL,
                LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISNULL,
                LogCodeConst.DanmuLogCode.CLIENT_HISTORYDANMU_ISNULL,
                LogCodeConst.DanmuLogCode.CLIENT_TIMERDANMU_ISNULL,
                LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISMORE
        };
        for(int i=0; i<typeArray.length; i++) {
            alarmCacheService.removeAlarmCount(addressId, typeArray[i]);
            alarmCacheService.removeAlarmCount(addressId, typeArray[i]);
        }
    }

    public void removeDanmuAlarmCacheIfDanmuIsMore(int danmuCount,String addressId){
        if(danmuCount<=15){
            //清除弹幕过量的缓存 和 和时间
            alarmCacheService.removeAlarmCount(addressId, LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISMORE);
            alarmCacheService.removeAlarmCount(addressId,LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISMORE);
        }
        //清除没有弹幕的计数 和 时间
        alarmCacheService.removeAlarmCount(addressId,LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISNULL);
        alarmCacheService.removeAlarmCount(addressId,LogCodeConst.DanmuLogCode.CLIENT_DANMU_ISNULL);
    }
}
