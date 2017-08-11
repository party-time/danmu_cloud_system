package cn.partytime.cache.alarm;

import cn.partytime.common.cachekey.alarm.AlarmCacheKey;
import cn.partytime.common.constants.CommonConst;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdressAlarmCacheService {

    @Autowired
    private RedisService redisService;

    private String getKey(String ... args){
        StringBuffer buffer = new StringBuffer(AlarmCacheKey.BASE_TYPE+ CommonConst.COLON);
        if(args!=null && args.length>0){
            for(String arg:args){
                buffer.append(arg+CommonConst.COLON);
            }
            buffer.append(CommonConst.COUNT);
        }
        return buffer.toString();
    }

    /**
     * 获取报警请求次数
     */
    public int findPreDanmuAlarmCount(String ... args){
        String key = getKey(args);
        return IntegerUtils.objectConvertToInt(redisService.get(key));
    }


    /**
     * @param time 时间
     * @param args 参数
     * 获取报警请求次数计数
     */
    public void addPreDanmuAlarmCount(long time,String ... args){
        String key = getKey(args);
        redisService.incrKey(key,1);

        redisService.expire(key,time==0?60*60*24:time);
    }

    /**
     * 报警请求次数计数清零
     */
    public void removePreDanmuAlarmCount(String ... args ){
        String key = getKey(args);
        redisService.expire(key,0);
    }

}
