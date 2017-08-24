package cn.partytime.cache.collector;


import cn.partytime.common.cachekey.AdminUserCacheKey;
import cn.partytime.common.cachekey.collector.CollectorCacheKey;
import cn.partytime.common.constants.CommonConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.common.util.LongUtils;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CollectorCacheService {

    @Autowired
    private RedisService redisService;

    public void setCollectorIpRelation(String addressId,String ip){
        String key = CollectorCacheKey.COLLECTOR_ADDRESS_IP_CACHEC_LIST+addressId;
        //redisService.setValueToList(key,ip);
        redisService.setSortSet(key,0,ip);
    }

    public void setClientCount(int type,String addressId,String ip,int count){
        String key = CollectorCacheKey.BASE_KEY+CommonConst.COLON+addressId+CommonConst.COLON+CommonConst.COLON+ip+CommonConst.COLON+type;
        redisService.set(key,count);
    }

    public int  getClientCount(int type,String addressId){
        String key = CollectorCacheKey.COLLECTOR_ADDRESS_IP_CACHEC_LIST+addressId;
        Set<String> stringSet =  redisService.getSortSetByRnage(key,0,-1,true);
        int count = 0;
        if(stringSet!=null && stringSet.size()>0){
            for(String ip:stringSet){
                key = CollectorCacheKey.BASE_KEY+CommonConst.COLON+addressId+CommonConst.COLON+CommonConst.COLON+ip+CommonConst.COLON+type;
                count+=  IntegerUtils.objectConvertToInt(redisService.get(key));
            }
        }
        return count;
    }


    /*public void setClientCount(int type,String addressId,String ip,int count){
        String key = CollectorCacheKey.COLLECTOR_CLIENT_CACHE_SORTSET + addressId+ CommonConst.COUNT+type;
        Map<String,Object> map = new HashMap<>();
        map.put("ip",ip);
        map.put("count",count);
        redisService.setSortSet(key,count, JSON.toJSONString(map));
        redisService.expire(key,60*60*5);
    }

    public int getClientCount(String addressId,int type){
        String key = CollectorCacheKey.COLLECTOR_CLIENT_CACHE_SORTSET + addressId+ CommonConst.COUNT+type;
        Set<String> stringSet = redisService.getSortSetByRnage(key,0,1,true);
        int count =0;
        if(stringSet!=null && stringSet.size()>0) {
            for (String str : stringSet) {
                Map<String, Object> map = (Map<String, Object>) JSON.parseObject(JSON.toJSONString(str));
                count += IntegerUtils.objectConvertToInt(map.get("count"));
            }
        }
        return count;
    }*/

    public void setFlashOfflineTime(String addressId){
        String key = CollectorCacheKey.COLLECTOR_FLASH_CLIENT_OFFLINE_TIME+addressId;
        redisService.set(key, DateUtils.getCurrentDate().getTime());
        redisService.expire(key,60*60*10);
    }
    public long findFlashOfflineTime(String addressId){
        String key = CollectorCacheKey.COLLECTOR_FLASH_CLIENT_OFFLINE_TIME+addressId;
        return LongUtils.objectConvertToLong(redisService.get(key));
    }

    public void removeFlashOfflineTime(String addressId){
        String key = CollectorCacheKey.COLLECTOR_FLASH_CLIENT_OFFLINE_TIME+addressId;
        redisService.expire(key,0);
    }
}
