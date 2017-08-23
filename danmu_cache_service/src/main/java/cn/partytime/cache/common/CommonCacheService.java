package cn.partytime.cache.common;


import cn.partytime.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommonCacheService {

    @Autowired
    private RedisService redisService;

    public void removeCache(String key){
        redisService.expire(key,0);
    }

}
