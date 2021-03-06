package cn.partytime.cache.user;

import cn.partytime.common.cachekey.wechat.WechatUserCountKey;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class WechatUserCountCacheService {

    @Autowired
    private RedisService redisService;


    public void setWechatUserAddress(String addressId){
        String key = WechatUserCountKey.WECHATUSER_ADDRESS_SORTSET;
        redisService.setSortSet(key,0,addressId);
        redisService.expire(key,60*60*1);
    }

    public Set<String> getWechatUserAddress(){
        String key = WechatUserCountKey.WECHATUSER_ADDRESS_SORTSET;
        return redisService.getSortSetByRnage(key,0,-1,true);
    }


    public void addWechatUser(String addressId){
        String key = WechatUserCountKey.WECHATUSER_COUNT+addressId;
        redisService.incrKey(key,1);
        redisService.expire(key,60*5);
    }

    public long getWechatUserCount(String addressId){
        String key = WechatUserCountKey.WECHATUSER_COUNT+addressId;
        return IntegerUtils.objectConvertToInt(redisService.get(key));
    }

    public void clearUserCountCacheData(String addressId){
        String key = WechatUserCountKey.WECHATUSER_COUNT+addressId;
        redisService.expire(key,0);
    }
}
