package cn.partytime.logicService;

import cn.partytime.common.cachekey.AddressCacheKey;
import cn.partytime.common.cachekey.ClientCacheKey;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.common.util.LongUtils;
import cn.partytime.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlashClientLogicService {

    @Autowired
    private RedisService redisService;

    /*public void addClientAlarmCount(int count,String addressId){
        String key = ClientCacheKey.CLIENT_OFFLINE_ALARM_COUNT+addressId;
        redisService.incrKey(key,count);
    }

    public int findClientAlarmCount(String addressId){
        String key = ClientCacheKey.CLIENT_OFFLINE_ALARM_COUNT+addressId;
        Object object = redisService.get(key);
        return IntegerUtils.objectConvertToInt(object);
    }


    public long getClientOfflineTime(String addressId){
        String key = ClientCacheKey.ClIENT_OFFLINE_TIME+addressId;
        Object object = redisService.get(key);
        if(object==null){
            return LongUtils.objectConvertToLong(object);
        }
        return 0;
    }

    public int  findClientFlashCount(String addressId){
        String key = AddressCacheKey.ADDRESS_FLASH_CLIENT_COUNT+addressId;
        return IntegerUtils.objectConvertToInt(redisService.get(key));
    }*/
}
