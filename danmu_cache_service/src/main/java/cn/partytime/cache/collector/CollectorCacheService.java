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

import java.util.*;

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
        String key = CollectorCacheKey.BASE_KEY+CommonConst.COLON+addressId+CommonConst.COLON+ip+CommonConst.COLON+type;
        redisService.set(key,count);
    }

    public int  getClientCount(int type,String addressId){
        String key = CollectorCacheKey.COLLECTOR_ADDRESS_IP_CACHEC_LIST+addressId;
        Set<String> stringSet =  redisService.getSortSetByRnage(key,0,-1,true);
        int count = 0;
        if(stringSet!=null && stringSet.size()>0){
            for(String ip:stringSet){
                key = CollectorCacheKey.BASE_KEY+CommonConst.COLON+addressId+CommonConst.COLON+ip+CommonConst.COLON+type;
                count+=  IntegerUtils.objectConvertToInt(redisService.get(key));
            }
        }
        return count;
    }

    public void setFlahOfflineCLient(String addressId,String registerCode){
        String key = CollectorCacheKey.COLLECTOR_FLASH_CLIENT_OFFLINE_LIST+addressId;
        redisService.setSortSet(key,0,registerCode);
        redisService.expire(key,60*60*24);
    }

    public void removeFlahOfflineCLient(String addressId,String registerCode){
        String key = CollectorCacheKey.COLLECTOR_FLASH_CLIENT_OFFLINE_LIST+addressId;
        redisService.deleteSortData(key,registerCode);
    }

    public List<String> findFlahOfflineCLientList(String addressId){
        String key = CollectorCacheKey.COLLECTOR_FLASH_CLIENT_OFFLINE_LIST+addressId;
        List<String> registerCodeList = new ArrayList<String>();
        Set<String> registerSet = redisService.getSortSetByRnage(key,0,-1,true);
        if(registerSet!=null && registerSet.size()>0){
            registerSet.forEach(set->registerCodeList.add(set));
        }
        return registerCodeList;
    }


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
