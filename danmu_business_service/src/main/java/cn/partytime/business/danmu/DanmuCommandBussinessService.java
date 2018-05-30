package cn.partytime.business.danmu;

import cn.partytime.common.cachekey.DanmuCacheKey;
import cn.partytime.common.constants.ProtocolConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.LongUtils;
import cn.partytime.common.util.NumberUtils;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 2018/5/16.
 */

@Service
@Slf4j
public class DanmuCommandBussinessService {

    @Autowired
    private RedisService redisService;


    /**
     * 向客户端发送弹幕
     * @param addressId
     * @param messageObject
     */
    public void pubMessageCollectorServer(String addressId,Map<String,Object> messageObject){
        log.info("弹幕审核状态:{},直接广播给客户端:{}",JSON.toJSONString(messageObject));
        String key = DanmuCacheKey.PUB_DANMU_CACHE_LIST + addressId;
        redisService.setValueToList(key, JSON.toJSONString(messageObject));
        redisService.expire(key, 60 * 60 * 1);
        redisService.subPub("addressId:danmu", addressId);
    }


    /**
     * 从弹幕队列中获取弹幕
     * @param addressId
     * @return
     */
    public Object getDanmuFromPubDanmuList(String addressId) {
        String key = DanmuCacheKey.PUB_DANMU_CACHE_LIST + addressId;
        return redisService.popFromList(key);
    }


    /**
     * 将弹幕放入到未发送队列
     * @param addressId
     * @param messageObject
     */
    public void pubDanmuToNotSendQueue(String addressId,Map<String,Object> messageObject){
        log.info("弹幕审核状态:{},直接广播给客户端:{}",JSON.toJSONString(messageObject));
        String key = DanmuCacheKey.PUB_DANMU_CACHE_NOTE_SEND_LIST + addressId;
        redisService.setValueToList(key, JSON.toJSONString(messageObject));
    }


    /**
     * 从未发送队列中取出弹幕
     * @param addressId
     * @return
     */
    public Object getDanmuFromNotSendQueue(String addressId){
        String key = DanmuCacheKey.PUB_DANMU_CACHE_NOTE_SEND_LIST + addressId;
        return redisService.popFromListFromRight(key);
    }


    /**
     * 获取未发送队列的长度
     * @param addressId
     * @return
     */
    public long getNotSendQueueSize(String addressId){
        String key = DanmuCacheKey.PUB_DANMU_CACHE_NOTE_SEND_LIST + addressId;
        return redisService.listsize(key);
    }


    /**
     * 支付弹幕队列
     */
    public void putIntoPayDanmuQueue(String addressId,String id){
        String key = DanmuCacheKey.PUB_DANMU_PAY_SORTSET + addressId;
        long score = DateUtils.getCurrentDate().getTime();
        redisService.setSortSet(key,score,id);
    }

    /**
     * 获取支付弹幕列表长度
     * @param addressId
     * @return
     */
    public long getPayDanmuQueueSize(String addressId){
        String key = DanmuCacheKey.PUB_DANMU_PAY_SORTSET + addressId;
        return redisService.getSortSetSize(key,0,-1);
    }

    /**
     * 从支付弹幕列表清除弹幕
     * @param addressId
     * @param id
     */
    public void removePayDanmuQueueSize(String addressId,String id){
        String key = DanmuCacheKey.PUB_DANMU_PAY_SORTSET + addressId;
        redisService.deleteSortData(key,id);
    }

    public Set<String> getPayDanmuQueueBeforeScore(String addressId,Double minScore,Double maxScore){
        String key = DanmuCacheKey.PUB_DANMU_PAY_SORTSET + addressId;
        log.info("key:{}",key);
        return  redisService.findSortSetWithInScore(key,minScore,maxScore);
    }

}
