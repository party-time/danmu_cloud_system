package cn.partytime.cache.admin;


import cn.partytime.common.cachekey.admin.AdminUserCacheKey;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.common.util.LongUtils;
import cn.partytime.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CheckAdminCacheService {

    @Autowired
    private RedisService redisService;


    public void setAdminOfflineTime(int type){
        String key = AdminUserCacheKey.CHECK_AMIN_OFFLINE_TIME+type;
        redisService.set(key, DateUtils.getCurrentDate().getTime());
        redisService.expire(key,60*60*10);
    }
    public long findAdminOfflineTime(int type){
        String key = AdminUserCacheKey.CHECK_AMIN_OFFLINE_TIME+type;
        return LongUtils.objectConvertToLong(redisService.get(key));
    }

    public void addCheckAdminCount(int type,int count){
        String key ="";
        if(type==0){
            key = AdminUserCacheKey.CHECK_PARTY_ADMIN_ONLINE_COUNT;
        }else{
            key = AdminUserCacheKey.CHECK_FILM_ADMIN_ONLINE_COUNT;
        }
        redisService.incrKey(key,count);
        redisService.expire(key,60*60*10);
    }

    public void setCheckAdminCount(int type,int count){
        String key ="";
        if(type==0){
            key = AdminUserCacheKey.CHECK_PARTY_ADMIN_ONLINE_COUNT;
        }else{
            key = AdminUserCacheKey.CHECK_FILM_ADMIN_ONLINE_COUNT;
        }
        redisService.set(key,count);
        redisService.expire(key,60*60*10);
    }

    public int getCheckAdminCount(int type){
        String key ="";
        if(type==0){
            key = AdminUserCacheKey.CHECK_PARTY_ADMIN_ONLINE_COUNT;
        }else{
            key = AdminUserCacheKey.CHECK_FILM_ADMIN_ONLINE_COUNT;
        }
        return IntegerUtils.objectConvertToInt(redisService.get(key));
    }
}
