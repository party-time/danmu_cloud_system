package cn.partytime.cache.alarm;

import cn.partytime.common.cachekey.alarm.AlarmCacheKey;
import cn.partytime.common.constants.CommonConst;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.common.util.LongUtils;
import cn.partytime.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlarmCacheService {

    @Autowired
    private RedisService redisService;

    private String getCountKey(String ... args){
        StringBuffer buffer = new StringBuffer(AlarmCacheKey.BASE_TYPE+ CommonConst.COLON);
        if(args!=null && args.length>0){
            for(String arg:args){
                buffer.append(arg+CommonConst.COLON);
            }
            buffer.append(CommonConst.COUNT);
        }
        return buffer.toString();
    }

    private String getTimeKey(String ... args){
        StringBuffer buffer = new StringBuffer(AlarmCacheKey.BASE_TYPE+ CommonConst.COLON);
        if(args!=null && args.length>0){
            for(String arg:args){
                buffer.append(arg+CommonConst.COLON);
            }
            buffer.append(CommonConst.TIME);
        }
        return buffer.toString();
    }

    /**
     * 获取报警请求次数
     */
    public int findAlarmCount(String ... args){
        String key = getCountKey(args);
        return IntegerUtils.objectConvertToInt(redisService.get(key));
    }


    /**
     * @param expireTime 时间
     * @param args 参数
     * 获取报警请求次数计数
     */
    public void addAlarmCount(long expireTime,String ... args){
        String key = getCountKey(args);
        redisService.incrKey(key,1);
        redisService.expire(key,expireTime==0?60*60*24:expireTime);
    }
    /**
     * 报警请求次数计数清零
     */
    public void removeAlarmCount(String ... args ){
        String key = getCountKey(args);
        redisService.expire(key,0);
    }

    /**
     * 告警时间
     * @param time
     * @param expireTime
     * @param args
     */
    public void addAlarmTime(long time,long expireTime,String... args){
        String key = getTimeKey(args);
        redisService.set(key,time);
        redisService.expire(key,expireTime==0?60*60*24:expireTime);
    }

    /**
     * 获取报警时间
     * @param args
     * @return
     */
    public long findAlarmTime(String... args){
        String key = getTimeKey(args);
        return LongUtils.objectConvertToLong(redisService.get(key));
    }

    public void removeAlarmTime(String... args){
        String key = getTimeKey(args);
        redisService.expire(key,0);
    }

}
