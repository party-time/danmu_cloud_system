package cn.partytime.cache.party;

import cn.partytime.common.cachekey.party.PartyCacheKey;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PartyCacheService {

    @Autowired
    private RedisService redisService;

    public int getPartyDensity(String partyId){
        String key = PartyCacheKey.PARTY_DENSITY + partyId;
        return IntegerUtils.objectConvertToInt(redisService.get(key));
    }

    /**
     * 保存活动的弹幕密度
     * @param partyId
     * @param dmDensity
     * @param time
     */
    public void setPartyDensity(String partyId,int dmDensity,long time){
        String key = PartyCacheKey.PARTY_DENSITY + partyId;
        redisService.set(key,dmDensity);
        if(time==0){
            redisService.expire(key,60*60*24*60);
        }else{
            redisService.expire(key,time);
        }

    }


}
