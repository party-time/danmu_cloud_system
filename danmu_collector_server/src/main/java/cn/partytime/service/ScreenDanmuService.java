package cn.partytime.service;

import cn.partytime.common.cachekey.ScreenClientCacheKey;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.dataRpc.RpcPreDanmuService;
import cn.partytime.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lENOVO on 2016/10/10.
 */

@Component
public class ScreenDanmuService {

    private static final Logger logger = LoggerFactory.getLogger(ScreenDanmuService.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private RpcPartyService rpcPartyService;

    @Autowired
    private RpcPreDanmuService rpcPreDanmuService;

    public void addScreenDanmuCount(String addressId){
        String key = ScreenClientCacheKey.SCREEN_DANMU_COUNT+addressId;
        redisService.incrKey(key,1);
        redisService.expire(key,60*60*30);

        key = ScreenClientCacheKey.SCREEN_DANMU_Time+addressId;
        redisService.set(key, DateUtils.getCurrentDate().getTime());
    }


    public void setScreenDanmuCount(String addressId,int danmuCount){
        String key = ScreenClientCacheKey.SCREEN_DANMU_COUNT+addressId;
        redisService.set(key,danmuCount);
        redisService.expire(key,60*60*30);

        key = ScreenClientCacheKey.SCREEN_DANMU_Time+addressId;
        redisService.set(key, DateUtils.getCurrentDate().getTime());
    }

    public int getAddressDanmuCount(String addressId){
        String key = ScreenClientCacheKey.SCREEN_DANMU_COUNT+addressId;
        Object object = redisService.get(key);
        int count =0;
        if(object!=null){
            count = IntegerUtils.objectConvertToInt(object);
        }

        logger.info("当前客户端的弹幕数量是=========================>{}",count);
        return count;
    }

    public long getlastDanmuTime(String addressId){
        String key = ScreenClientCacheKey.SCREEN_DANMU_Time+addressId;
        Object object = redisService.get(key);
        if(object!=null){
            return Long.parseLong(String.valueOf(object));
        }
        return 0;
    }



    /**
     * 屏幕不足的弹幕数了
     *
     * @return
     */
    public int danmuCount(String addressId,int danmuCount,String partyId) {
        int danmuDensity = rpcPreDanmuService.getPartyDanmuDensity(partyId);
        logger.info("当前活动{}的弹幕密度是:{}",partyId,danmuDensity);
        if (danmuCount < danmuDensity) {
            return danmuDensity - danmuCount;
        }
        return 0;
    }

}
