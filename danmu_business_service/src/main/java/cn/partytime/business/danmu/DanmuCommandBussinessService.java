package cn.partytime.business.danmu;

import cn.partytime.common.cachekey.danmu.DanmuCacheKey;
import cn.partytime.common.util.DateUtils;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        String key = DanmuCacheKey.PUB_DANMU_CACHE_NOT_SEND_LIST + addressId;
        redisService.setValueToList(key, JSON.toJSONString(messageObject));
    }

    /**
     * 从未发送队列中取出弹幕
     * @param addressId
     * @return
     */
    public Object getDanmuFromNotSendQueue(String addressId){
        String key = DanmuCacheKey.PUB_DANMU_CACHE_NOT_SEND_LIST + addressId;
        return redisService.popFromListFromRight(key);
    }

    /**
     * 获取未发送队列的长度
     * @param addressId
     * @return
     */
    public long getDanmuFromNotSendQueueSize(String addressId){
        String key = DanmuCacheKey.PUB_DANMU_CACHE_NOT_SEND_LIST + addressId;
        return redisService.getListLength(key);
    }



    /**
     *  支付成功，发送成功 弹幕队列存入数据
     */
    public void putIntoPayDanmuSendSuccessQueue(String addressId,String id){
        String key = DanmuCacheKey.PUB_DANMU_PAY_SEND_SORTSET + addressId;
        long score = DateUtils.getCurrentDate().getTime();
        redisService.setSortSet(key,score,id);
    }

    /**
     * 从 支付成功，发送成功 弹幕队列 清除数据
     * @param addressId
     * @param id
     * @param id
     */
    public void removePayDanmuSendSuccessQueueSize(String addressId,String id){
        String key = DanmuCacheKey.PUB_DANMU_PAY_SEND_SORTSET + addressId;
        redisService.deleteSortData(key,id);
    }

    /**
     *  从 支付成功，发送成功 弹幕队列 取出数据
     * @param addressId
     * @param minScore
     * @param maxScore
     * @return
     */
    public Set<String> getPayDanmuSendSuccessBeforeScore(String addressId,Double minScore,Double maxScore){
        String key = DanmuCacheKey.PUB_DANMU_PAY_SEND_SORTSET + addressId;
        return  redisService.findSortSetWithInScore(key,minScore,maxScore);
    }

    /**
     * 从 支付成功，发送成功 弹幕队列 取出所有数据
     * @param addressId
     * @return
     */
    public Set<String> getPayDanmuSendSuccessQueue(String addressId){
        String key = DanmuCacheKey.PUB_DANMU_PAY_SEND_SORTSET + addressId;
        return  redisService.getSortSetByRnage(key,0,-1,true);
    }

    /**
     * 获取 支付成功，发送成功 弹幕队列 的长度
     * @param addressId
     * @return
     */
    public long getPayDanmuSendSuccessQueueSize(String addressId){
        String key = DanmuCacheKey.PUB_DANMU_PAY_SEND_SORTSET + addressId;
        return  redisService.getListLength(key);
    }


    /**
     * 清除 支付成功，发送成功 弹幕队列
     * @param addressId
     * @return
     */
    public void clearPayDanmuSendSuccessQueue(String addressId){
        String key = DanmuCacheKey.PUB_DANMU_PAY_SEND_SORTSET + addressId;
        redisService.expire(key,0);
    }




    /**
     * 支付成功，未发送成功 弹幕队列存入数据
     */
    public void putIntoPayDanmuNotSendQueue(String addressId,String id){
        String key = DanmuCacheKey.PUB_DANMU_PAY_NOT_SEND_SORTSET + addressId;
        long score = DateUtils.getCurrentDate().getTime();
        redisService.setSortSet(key,score,id);
    }
    /**
     * 支付成功，未发送成功 弹幕队列清除
     * @param addressId
     * @param id
     * @param id
     */
    public void removePayDanmuNotSendQueueSize(String addressId,String id){
        String key = DanmuCacheKey.PUB_DANMU_PAY_NOT_SEND_SORTSET + addressId;
        redisService.deleteSortData(key,id);
    }

    /**
     *  从支付成功，未发送成功 弹幕队列取出所有数据
     * @param addressId
     * @return
     */
    public Set<String> getPayDanmuNotSendQueue(String addressId){
        String key = DanmuCacheKey.PUB_DANMU_PAY_NOT_SEND_SORTSET + addressId;
        return  redisService.getSortSetByRnage(key,0,-1,true);
    }

    /**
     *  从支付成功，未发送成功 弹幕队列长度
     * @param addressId
     * @return
     */
    public long getPayDanmuNotSendQueueSize(String addressId){
        String key = DanmuCacheKey.PUB_DANMU_PAY_NOT_SEND_SORTSET + addressId;
        return redisService.getListLength(key);
    }

    /**
     * 清除 支付成功，发送成功 弹幕队列
     * @param addressId
     * @return
     */
    public void clearPayDanmuNotSendQueue(String addressId){
        String key = DanmuCacheKey.PUB_DANMU_PAY_NOT_SEND_SORTSET + addressId;
        redisService.expire(key,0);
    }


}
