package cn.partytime.cache.admin;


import cn.partytime.common.cachekey.admin.AdminUserCacheKey;
import cn.partytime.common.constants.CommonConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.common.util.LongUtils;
import cn.partytime.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

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

    /**
     * 增加一个在线管理员
     * @param type
     * @param count
     */
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

    /**
     * 重置在线管理员
     * @param type
     * @param count
     */
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

    /**
     * 获取现在管理员的数量
     * @param type
     * @return
     */
    public int getCheckAdminCount(int type){
        String key ="";
        if(type==0){
            key = AdminUserCacheKey.CHECK_PARTY_ADMIN_ONLINE_COUNT;
        }else{
            key = AdminUserCacheKey.CHECK_FILM_ADMIN_ONLINE_COUNT;
        }
        return IntegerUtils.objectConvertToInt(redisService.get(key));
    }

    /**
     * 将在线管理员id存入到sortset
     * @param type
     * @param admingId
     */
    public void addOnlineAdminSortSet(int type,String admingId){
        String key = AdminUserCacheKey.CHECK_ADMIN_ONLINE_SORTSET+type;
        redisService.setSortSet(key,0,admingId);
        redisService.expire(key,60*60*2);
    }

    /**
     * 从在线管理员sortset 删除管理员
     * @param type
     * @param admingId
     */
    public void removeOnlineAdminSortSet(int type,String admingId){
        String key = AdminUserCacheKey.CHECK_ADMIN_ONLINE_SORTSET+type;
        redisService.deleteSortData(key,admingId);
        redisService.expire(key,60*60*2);
    }


    /**
     * 获取在线的管理员
     * @param type
     * @return
     */
    public Set<String> getOnlineAmdinSortSet(int type){
        String key = AdminUserCacheKey.CHECK_ADMIN_ONLINE_SORTSET+type;
        return redisService.getSortSetByRnage(key,0,0,true);
    }

    /**
     * 将离线线管理员id存入到sortset
     * @param type
     * @param admingId
     */
    public void addOfflineAdminSortSet(int type,String admingId){
        String key = AdminUserCacheKey.CHECK_ADMIN_OFFLINE_SORTSET+type;
        redisService.setSortSet(key,0,admingId);
        redisService.expire(key,60*60*2);
    }

    /**
     * 从离线管理员sortset 删除管理员
     * @param type
     * @param admingId
     */
    public void removeOfflineAdminSortSet(int type,String admingId){
        String key = AdminUserCacheKey.CHECK_ADMIN_OFFLINE_SORTSET+type;
        redisService.deleteSortData(key,admingId);
        redisService.expire(key,60*60*2);
    }

    /**
     * 获取离线的管理员
     * @param type
     * @return
     */
    public Set<String> getOfflineAdminSortSet(int type){
        String key = AdminUserCacheKey.CHECK_ADMIN_OFFLINE_SORTSET+type;
        return redisService.getSortSetByRnage(key,0,0,true);
    }
}
