package cn.partytime.service;

import cn.partytime.common.cachekey.AddressCacheKey;
import cn.partytime.common.cachekey.CommandCacheKey;
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
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    /**
     * 将命令压入队列
     * @param addressId
     * @param commandMap
     */
    public void setCommandToList(String addressId,Map<String,Object> commandMap){
        String repeatCommandCacheKey = CommandCacheKey.PUB_COMMAND_PARTYSTATUS_QUEUE_CACHE+addressId;
        //设置到预发送队列
        redisService.setValueToList(repeatCommandCacheKey, JSON.toJSONString(commandMap));
        redisService.expire(repeatCommandCacheKey,60*60*2);
    }

    /**
     * 获取一个命令
     * @param addressId
     * @return
     */
    public Map<String,Object> getFirstCommandFromCache(String addressId){
        String key = CommandCacheKey.PUB_COMMAND_PARTYSTATUS_QUEUE_CACHE+addressId;
        String firstCommandKey = CommandCacheKey.PUB_COMMAND_PARTYSTATUS_QUEUE_TEMP_CACHE+addressId;
        //设置到预发送队列
        Object firstCommandObject = redisService.get(firstCommandKey);
        if(firstCommandObject!=null){
            return (Map<String,Object>)JSON.parse(String.valueOf(firstCommandObject));
        }else{
            Object object = redisService.popFromList(key);
            if(object!=null){
                redisService.set(firstCommandKey,object);
                return (Map<String,Object>)JSON.parse(String.valueOf(object));
            }
        }
        return null;
    }

    public int findTempCommandCount(String addressId){
        String key = CommandCacheKey.PUB_COMMAND_PARTYSTATUS_QUEUE_TEMP_COUNT_CACHE+addressId;
        Object object = redisService.get(key);
        return IntegerUtils.objectConvertToInt(object);
    }

    public void addTempCommandCount(String addressId){
        String key = CommandCacheKey.PUB_COMMAND_PARTYSTATUS_QUEUE_TEMP_COUNT_CACHE+addressId;
        redisService.incrKey(key,1);
    }

    public void removeTempCommandCount(String addressId){
        String key = CommandCacheKey.PUB_COMMAND_PARTYSTATUS_QUEUE_TEMP_COUNT_CACHE+addressId;
        redisService.expire(key,0);
    }


    /**
     * 清除命令缓存
     * @param addressId
     */
    public void removeCommandCache(String addressId){
        String key = CommandCacheKey.PUB_COMMAND_PARTYSTATUS_QUEUE_CACHE+addressId;
        String firstCommandKey = CommandCacheKey.PUB_COMMAND_PARTYSTATUS_QUEUE_TEMP_CACHE+addressId;
        String countKey = CommandCacheKey.PUB_COMMAND_PARTYSTATUS_QUEUE_TEMP_COUNT_CACHE+addressId;
        redisService.expire(key,0);
        redisService.expire(firstCommandKey,0);
        redisService.expire(countKey,0);
    }

    /**
     * 设置当前的命令
     * @param addressId
     * @param map
     */
    public void  setFirstCommandFromCache(String addressId,Map<String,Object> map){
        String firstCommandKey = CommandCacheKey.PUB_COMMAND_PARTYSTATUS_QUEUE_TEMP_CACHE+addressId;
        redisService.set(firstCommandKey,JSON.toJSONString(map));
        redisService.expire(firstCommandKey,60*60*2);
    }

    public void removeFirstCommandFromCache(String addressId){
        String firstCommandKey = CommandCacheKey.PUB_COMMAND_PARTYSTATUS_QUEUE_TEMP_CACHE+addressId;
        redisService.expire(firstCommandKey,0);
    }



}
