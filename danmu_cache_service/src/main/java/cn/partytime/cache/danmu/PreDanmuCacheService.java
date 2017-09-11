package cn.partytime.cache.danmu;


import cn.partytime.common.cachekey.danmu.PreDanmuCacheKey;
import cn.partytime.common.constants.CommonConst;
import cn.partytime.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PreDanmuCacheService {

    @Autowired
    private RedisService redisService;



    public Object findPreDanmu(String partyId,String libraryId){
        String key = PreDanmuCacheKey.PARTY_PREDANMU_CACHE_LIST + partyId+ CommonConst.COLON+libraryId;
        Object object =  redisService.popFromList(key);
        log.info("object:{}",object);
        return object;
    }


    public void removePreDanmuFromCache(String partyId,String libraryId){
        String key = PreDanmuCacheKey.PARTY_PREDANMU_CACHE_LIST + partyId+ CommonConst.COLON+libraryId;
        redisService.expire(key,0);
    }

    /**
     * 将预置弹幕添加到缓存中
     * @param partyId
     * @param libraryId
     * @param danmu
     */
    public void addPreDanmuIntoCacheUnderParty(String partyId,String libraryId, String danmu){
        String key = PreDanmuCacheKey.PARTY_PREDANMU_CACHE_LIST + partyId+ CommonConst.COLON+libraryId;
        redisService.setValueToList(key, danmu);
    }

    /**
     * 设置预置弹幕时间
     * @param partyId
     * @param libraryId
     * @param time
     */
    public void setPreDanmuIntoCacheUnderPartyTime(String partyId,String libraryId, long time){
        String key = PreDanmuCacheKey.PARTY_PREDANMU_CACHE_LIST + partyId+ CommonConst.COLON+libraryId;
        redisService.expire(key,time);
    }
}
