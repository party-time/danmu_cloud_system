package cn.partytime.cache.wechatmin;


import cn.partytime.common.cachekey.wechat.WechatMiniCacheKey;
import cn.partytime.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;

@Service
public class WechatMiniCacheService {

    @Autowired
    private RedisService redisService;

    public void setWechatMiniUserCache(String sessionId,String unionId){
        String key = WechatMiniCacheKey.WECHAT_Mini_SESSION_KEY + sessionId;
        redisService.set(key,unionId);
        redisService.expire(key,60*60*2);
    }

    public Object  getWechatMiniUserCache(String sessionId){
        String key = WechatMiniCacheKey.WECHAT_Mini_SESSION_KEY + sessionId;
        return redisService.get(key);
    }

}
