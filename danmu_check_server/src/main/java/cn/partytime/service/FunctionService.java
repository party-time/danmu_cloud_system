package cn.partytime.service;

import cn.partytime.common.cachekey.FunctionControlCacheKey;
import cn.partytime.common.constants.PartyConst;
import cn.partytime.common.util.BooleanUtils;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.redis.service.RedisService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lENOVO on 2016/9/12.
 */

@Service
public class FunctionService {


    @Autowired
    private RedisService redisService;

    /**
     * 获取延迟时间
     *
     * @param partyId
     * @return
     */
    public int getDelayTime(String partyId) {
        String key = FunctionControlCacheKey.FUNCITON_CONTROL_DELAYSECOND + partyId;
        int second = PartyConst.delaySecond;
        Object object = redisService.get(key);
        second = (object == null ? second : IntegerUtils.objectConvertToInt(object));
        return second;
    }

    /**
     * 判断测试模式是否开启
     *
     * @param partyId
     * @return
     */
    public boolean testModeIsOpen(String partyId) {
        String key = FunctionControlCacheKey.FUNCITON_CONTROL_TESTMODEL + partyId;
        Object object = redisService.get(key);
        if (object != null) {
            return BooleanUtils.objectConvertToBoolean(object);
        }
        return false;
    }


    /**
     * 查询预置弹幕状态
     *
     * @param partyId
     * @return
     */
    public boolean getpreDanmuStatus(String partyId) {
        String key = FunctionControlCacheKey.FUNCITON_CONTROL_PREDANMU + partyId;
        boolean status = false;
        Object object = redisService.get(key);
        if(object!=null){
            return BooleanUtils.objectConvertToBoolean(object);
        }
        return status;
    }


    /**
     * 动画特效
     *
     * @param partyId
     * @return
     */
    public String getSpecialMovStatus(String partyId) {
        String key = FunctionControlCacheKey.FUNCITON_CONTROL_SPECIALMOV + partyId;
        String id = StringUtil.EMPTY_STRING;
        Object object = redisService.get(key);
        if (object != null) {
            return String.valueOf(object);
        }
        return id;
    }


    /**
     * 获取弹幕密度
     * @param partyId
     * @return
     */
    public int getDanmuDensity(String partyId) {
        String key = FunctionControlCacheKey.FUNCITON_CONTROL_DANMU_DENSITY + partyId;
        int status = PartyConst.danmuDensity;
        Object object = redisService.get(key);
        status = IntegerUtils.objectConvertToInt(object) == 0 ? status : IntegerUtils.objectConvertToInt(object);
        return status;
    }

}
