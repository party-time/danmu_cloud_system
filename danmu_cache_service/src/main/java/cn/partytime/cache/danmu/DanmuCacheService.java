package cn.partytime.cache.danmu;


import cn.partytime.common.cachekey.danmu.DanmuCacheKey;
import cn.partytime.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DanmuCacheService {

    @Autowired
    private RedisService redisService;

    public void setPartyDanmuToTempList(String partyId,Object object){
        String key = DanmuCacheKey.SEND_TEMP_DANMU_CACHE_LIST+partyId;
        redisService.setValueToList(key,object);
        redisService.expire(key,60*60*1);
    }

    /**
     * 从临时缓存队列中获取弹幕
     * @param partyId
     * @return
     */
    public Object getDanmuFromTempList(String partyId){
        String key = DanmuCacheKey.SEND_TEMP_DANMU_CACHE_LIST+partyId;
        return  redisService.popFromListFromRight(key);
    }



}
