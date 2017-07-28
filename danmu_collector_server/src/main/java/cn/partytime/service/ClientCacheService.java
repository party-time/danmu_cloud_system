package cn.partytime.service;

import cn.partytime.common.cachekey.AddressCacheKey;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.config.DanmuChannelRepository;
import cn.partytime.model.DanmuClientModel;
import cn.partytime.common.cachekey.ClientCacheKey;
import cn.partytime.common.cachekey.DanmuCacheKey;
import cn.partytime.common.constants.ClientConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.common.util.LongUtils;
import cn.partytime.redis.service.RedisService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lENOVO on 2016/9/7.
 */
@Service
public class ClientCacheService {

    private static final Logger logger = LoggerFactory.getLogger(ClientCacheService.class);

    @Autowired
    private RedisService redisService;

    /**
     * 获取广播队列中的弹幕信息
     *
     * @param addressId
     * @return
     */
    public Object getDanmuFromPubDanmuList(String addressId) {
        String key = DanmuCacheKey.PUB_DANMU_CACHE_LIST + addressId;
        return redisService.popFromList(key);
    }


    public void setClientIdIntoCache(String addressId,String registerCode){
        String key = ClientCacheKey.CLIENT_CACHE_ID_SORTSET + addressId;
        redisService.setSortSet(key,0,registerCode);
        redisService.expire(key,60*60*24*30);
    }

    public void removeClientFromCache(String addressId,String registerCode){
        String key = ClientCacheKey.CLIENT_CACHE_ID_SORTSET + addressId;
        redisService.deleteSortData(key,registerCode);
    }

    public long getClientSize(String addressId){
        String key = ClientCacheKey.CLIENT_CACHE_ID_SORTSET + addressId;
        return  redisService.findSortSetSize(key);
    }


    public void removeAlarmCache(String addressId){
        redisService.expire(ClientCacheKey.CLIENT_OFFLINE_ALARM_COUNT,0);
        redisService.expire(ClientCacheKey.ClIENT_OFFLINE_TIME+addressId,0);
    }



    public void setClientOffineLineTime(String addressId){
        String key = ClientCacheKey.ClIENT_OFFLINE_TIME+addressId;
        redisService.set(key, DateUtils.getCurrentDate().getTime());
        redisService.expire(key,60*60);
    }

    public void addClientFlashCount(String addressId){
        String key = AddressCacheKey.ADDRESS_FLASH_CLIENT_COUNT+addressId;
        redisService.incrKey(key,1);
    }
    public void removeClientFlashCount(String addressId){
        String key = AddressCacheKey.ADDRESS_FLASH_CLIENT_COUNT+addressId;
        redisService.decrKey(key,-1);
    }


}
