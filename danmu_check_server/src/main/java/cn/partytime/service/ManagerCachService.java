package cn.partytime.service;

import cn.partytime.common.cachekey.AdminTaskCacheKey;
import cn.partytime.common.constants.CommonConst;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.redis.service.RedisService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by lENOVO on 2016/10/11.
 */

@Component
public class ManagerCachService {
    private static final Logger logger = LoggerFactory.getLogger(ManagerCachService.class);

    @Autowired
    private RedisService redisService;


    /**
     * 当前通道可分配的任务数
     *
     * @param ChannelId
     * @param partyId
     * @return
     */
    public int appointTaskCount(String ChannelId, String partyId) {
        String key = AdminTaskCacheKey.ADMIN_PARTY_TASK_COUNT_KEY + partyId + CommonConst.COLON + ChannelId;
        Object countObject = redisService.get(key);
        int count = IntegerUtils.objectConvertToInt(countObject);
        logger.info("通道:{},活动:{},任务数:{}", ChannelId, partyId, count);
        return count;
    }

    public int appointTaskCount(String channelId) {
        String key = AdminTaskCacheKey.ADMIN_PARTY_TASK_COUNT_KEY + channelId;
        Object countObject = redisService.get(key);
        int count = IntegerUtils.objectConvertToInt(countObject);
        return count;
    }

    /**
     * 管理弹幕计数
     * 设置屏幕弹幕计数(-1)
     */
    public void addAppointCount(String channelId) {
        logger.info("通道:{},任务数计数操作", channelId);
        String key = AdminTaskCacheKey.ADMIN_PARTY_TASK_COUNT_KEY + channelId;
        redisService.incrKey(key, 1);
        redisService.expire(key, 60 * 60 * 4);
    }

    /**
     * 管理弹幕计数
     * 设置屏幕弹幕计数(-1)
     */
    public void addAppointCount(String channelId, String partyId) {
        logger.info("通道:{},活动:{},任务数计数操作", channelId, partyId);
        String key = AdminTaskCacheKey.ADMIN_PARTY_TASK_COUNT_KEY + partyId + CommonConst.COLON + channelId;
        redisService.incrKey(key, 1);
        redisService.expire(key, 60 * 60 * 4);
    }

    /**
     * 发送测试弹幕数计数
     *
     * @param addressId
     * @param partyId
     * @return
     */
    public void addtestDanmuCount(String partyId, String addressId) {
        logger.info("活动:{},地点:{},发送测试弹幕计数", partyId, addressId);
        String key = AdminTaskCacheKey.ADMIN_PARTY_TEST_DANMU_COUNT_KEY + partyId + CommonConst.COLON + addressId;
        redisService.incrKey(key, 1);
        redisService.expire(key, 5);

    }

    /**
     * 发送的测试弹幕数
     *
     * @param addressId
     * @param partyId
     * @return
     */
    public int testDanmuCount(String addressId, String partyId) {
        String key = AdminTaskCacheKey.ADMIN_PARTY_TEST_DANMU_COUNT_KEY + partyId + CommonConst.COLON + addressId;
        Object countObject = redisService.get(key);
        int count = IntegerUtils.objectConvertToInt(countObject);
        logger.info("获取活动:{},地点:{}已经发送过的测试弹幕数:{}", partyId, addressId, count);
        return count;
    }

    /**
     * 重置缓存测试弹幕数量
     *
     * @param addressId
     * @param partyId
     */
    public void resetTestDanmuCount(String addressId, String partyId) {
        String key = AdminTaskCacheKey.ADMIN_PARTY_TEST_DANMU_COUNT_KEY + partyId + CommonConst.COLON + addressId;
        redisService.expire(key, 0);
    }

    /***
     * 活动下离开一个管理员
     * @param partyId
     */
    public void subOnlineAdminCount(Channel channel,String partyId) {
        logger.info("活动{}离开一个管理员", partyId);

        //清除管理员弹幕数量
        String channelId = channel.id().asLongText();
        String managerCountKey = AdminTaskCacheKey.ADMIN_PARTY_TASK_COUNT_KEY + partyId + CommonConst.COLON + channelId;
        redisService.expire(managerCountKey, 0);

        String key = AdminTaskCacheKey.ADMIN_PARTY_TASK_COUNT_KEY + channelId;
        redisService.expire(key,0);

    }


}
