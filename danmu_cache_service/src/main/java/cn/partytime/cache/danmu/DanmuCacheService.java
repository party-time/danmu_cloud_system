package cn.partytime.cache.danmu;


import cn.partytime.common.cachekey.danmu.DanmuCacheKey;
import cn.partytime.common.constants.CommonConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.SetUtils;
import cn.partytime.redis.service.RedisService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class DanmuCacheService {

    @Autowired
    private RedisService redisService;


    public void setSendDanmuInfo(String danmuId,Object danmuInfo){
        String key= DanmuCacheKey.SEND_DANMU_INFO + danmuId;
        redisService.set(key,danmuInfo);
        redisService.expire(key,60*60*2);
    }

    public Object getSendDanmuInfo(String danmuId){
        String key= DanmuCacheKey.SEND_DANMU_INFO + danmuId;
        return redisService.get(key);
    }

    public void removeSendDanmuInfo(String danmuId){
        String key= DanmuCacheKey.SEND_DANMU_INFO + danmuId;
        redisService.expire(key,0);
    }




    /**
     * 向临时缓存队列中放入弹幕（活动）
     * @param partyId
     * @return
     */
    public void setPartyDanmuToTempList(String partyId,Object object){
        String key = DanmuCacheKey.SEND_TEMP_DANMU_CACHE_LIST+partyId;
        redisService.setValueToList(key,object);
        redisService.expire(key,60*60*1);
    }

    /**
     * 从临时缓存队列中获取弹幕(活动）
     * @param partyId
     * @return
     */
    public Object getDanmuFromTempList(String partyId){
        String key = DanmuCacheKey.SEND_TEMP_DANMU_CACHE_LIST+partyId;
        return  redisService.popFromListFromRight(key);
    }

    /**
     * 用户弹幕存入用户sortset(活动)
     * @param partyId
     * @param adminId
     * @param danmuId
     */
    public void addPartyDanmuToCheckUserSortSet(String partyId,String adminId,String danmuId){
        String key = DanmuCacheKey.SEND_USER_DANMU_CACHE_LIST+partyId+CommonConst.COLON+adminId;
        long time = DateUtils.getCurrentDate().getTime();
        redisService.setSortSet(key,time,danmuId);
        redisService.expire(key,60*60*10);
    }

    /**
     * 清除弹幕存入用户sortset(活动)
     * @param partyId
     * @param adminId
     * @param danmuId
     */
    public void reomvePartyDanmuFromCheckUserSortSet(String partyId,String adminId,String danmuId){
        String key = DanmuCacheKey.SEND_USER_DANMU_CACHE_LIST+partyId+CommonConst.COLON+adminId;
        redisService.deleteSortData(key,danmuId);
        //removeSendDanmuInfo(danmuId);

    }


    public Set<String> getAllPartyDanmuFromCheckUserSortSet(String partyId,String adminId){
        String key = DanmuCacheKey.SEND_USER_DANMU_CACHE_LIST+partyId+CommonConst.COLON+adminId;
        long time = DateUtils.getCurrentDate().getTime();
        return redisService.getSortSetByRnage(key,0,time,true);
    }

    public Object getOnePartyDanmuFromCheckUserSortSet(String partyId,String adminId){
        String key = DanmuCacheKey.SEND_USER_DANMU_CACHE_LIST+partyId+CommonConst.COLON+adminId;
        Set<String> set = redisService.getSortSetByRnage(key,0,1,true);
        if(SetUtils.checkSetIsNotNull(set)){
            return set.iterator().next();
        }
        return null;
    }

}
