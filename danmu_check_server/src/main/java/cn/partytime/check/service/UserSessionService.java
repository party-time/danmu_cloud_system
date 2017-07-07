package cn.partytime.check.service;

import cn.partytime.common.cachekey.AdminUserCacheKey;
import cn.partytime.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lENOVO on 2016/11/18.
 */

@Service
public class UserSessionService {

    @Autowired
    private RedisService redisService;

    /**
     * 判断session是否存在
     * @param cookieValue
     * @return
     */
    public Boolean checkAuthKey(String cookieValue){
        String key = AdminUserCacheKey.ADMIN_USER_CACHE_KEY+cookieValue;
        System.out.println("key======================="+redisService.get(key.replace(" ","")));

        redisService.set("ppppppppppppppp","234234234324");
        System.out.println("=================>"+redisService.get("mykey"));
        return redisService.isEXIST(key);
    }

    /**
     * 延长会话时长
     * @param cookieValue
     */
    public void addSessionTime(String cookieValue){
        String key = AdminUserCacheKey.ADMIN_USER_CACHE_KEY+cookieValue;
        redisService.expire(key,3600);
    }
}
