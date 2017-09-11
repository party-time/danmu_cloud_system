package cn.partytime.cache.danmu;


import cn.partytime.common.cachekey.danmu.PreDanmuLibraryCacheKey;
import cn.partytime.common.util.SetUtils;
import cn.partytime.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;

@Service
@Slf4j
public class PreDanmuLibraryCacheService {

    @Autowired
    private RedisService redisService;


    public Set<String> getLibraryIdFromCache(String partyId,Double minScore, Double maxScore,long startRange, long endRange,boolean orderByDesc){
        String key = PreDanmuLibraryCacheKey.PARTY_PREDANMU_LIBRARY_SORT_RULE +partyId;
        return redisService.findSortSetRangeByScoreWithScores(key,minScore,maxScore,startRange,endRange,true);
    }

    public void removeDanmuLibraryIdFromCache(String partyId,String value){
        String key = PreDanmuLibraryCacheKey.PARTY_PREDANMU_LIBRARY_SORT_RULE +partyId;
        redisService.deleteSortData(key,value);
    }

    public void setPreDanmuLibraryIntoCache(String partyId,String value,double score,long time){
        String key = PreDanmuLibraryCacheKey.PARTY_PREDANMU_LIBRARY_SORT_RULE +partyId;
        redisService.setSortSet(key,score,value);
        if(time==0){
            redisService.expire(key,60*60*24);
        }
    }

    public void removePreDanmuLibrary(String partyId){
        String key = PreDanmuLibraryCacheKey.PARTY_PREDANMU_LIBRARY_SORT_RULE +partyId;
        redisService.expire(key,0);
    }

}
