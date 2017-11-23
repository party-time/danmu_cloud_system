package cn.partytime.cache.projector;

import cn.partytime.common.cachekey.client.ProjectorCacheKey;
import cn.partytime.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectorCacheService {

    @Autowired
    private RedisService redisService;

    public void setProjectCloseCount(String addressId){
        String key = ProjectorCacheKey.PROJECTOR_CLIENT_CLOSE_COUNT+addressId;
        redisService.set(key,1);
        redisService.expire(key,60*60*24);
    }

    public Object getProjectCloseCount(String addressId){
        String key = ProjectorCacheKey.PROJECTOR_CLIENT_CLOSE_COUNT+addressId;
        return redisService.get(key);
    }

    public void clearProjectCloseCount(String addressId){
        String key = ProjectorCacheKey.PROJECTOR_CLIENT_CLOSE_COUNT+addressId;
        redisService.expire(key,0);
    }



}
