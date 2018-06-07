package cn.partytime.cache.client;

import cn.partytime.common.cachekey.client.ClientInfoCacheKey;
import cn.partytime.common.constants.CommonConst;
import cn.partytime.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * Created by admin on 2018/6/5.
 */

@Service
public class ClientInfoCacheService {


    @Autowired
    private RedisService redisService;

    /**
     *
     * @param addressId 地址
     * @param type 类型 flash:0  h5:1  java:2
     * @param registerCode
     */
    public void setClientRegisterCodeIntoSortSet(String addressId,String type,String registerCode,long score){
        String key = ClientInfoCacheKey.CLIENT_SORTSET+addressId+ CommonConst.COLON+type;
        redisService.setSortSet(key,score,registerCode);
    }

    public Set<String> findClientRegisterCodeIntoSortSet(String addressId, String type){
        String key = ClientInfoCacheKey.CLIENT_SORTSET+addressId+ CommonConst.COLON+type;
        return redisService.getSortSetByRnage(key,0,-1,true);
    }


    public void removeClientRegisterCodeIntoSortSet(String addressId,String type,String registerCode){
        String key = ClientInfoCacheKey.CLIENT_SORTSET+addressId+ CommonConst.COLON+type;
        redisService.deleteSortData(key,registerCode);
    }

    /**
     * 将客户端信息存入cache
     * @param registerCode
     * @param type
     * @param object
     */
    public void setClientToCache(String registerCode,String type,Object object){
        String key = ClientInfoCacheKey.CLIENT_INFO+registerCode+ CommonConst.COLON+type;
        redisService.set(key,object);
        redisService.expire(key,60*60);
    }

    /**
     * 将客户端信息从缓存中清除
     * @param registerCode
     * @param type
     */
    public void removeClientToCache(String registerCode,String type){
        String key = ClientInfoCacheKey.CLIENT_INFO+registerCode+ CommonConst.COLON+type;
        redisService.expire(key,0);
    }


    public String findClientFromCache(String registerCode,String type){
        String key = ClientInfoCacheKey.CLIENT_INFO+registerCode+ CommonConst.COLON+type;
        Object object = redisService.get(key);
        if(object!=null){
            return String.valueOf(object);
        }
        return null;
    }


}
