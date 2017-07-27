package cn.partytime.service;

import cn.partytime.common.cachekey.FunctionControlCacheKey;
import cn.partytime.common.cachekey.PreDanmuCacheKey;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by lENOVO on 2016/9/5.
 */

@Service
public class ClientPreDanmuService {

    private static final Logger logger = LoggerFactory.getLogger(ClientPreDanmuService.class);


    @Autowired
    private RedisService redisService;

    @Autowired
    private RpcPartyService rpcPartyService;


    /**
     * @param addressId
     * @param nowDate
     * @return
     */
    public Object getPreDanmu(String addressId, Date nowDate, String partyId) {
        logger.info("*********通过地址:{},活动:{}获取预制弹幕", addressId, partyId);
        Object preDanmuModel = findPreDanmu(addressId, nowDate, partyId);
        return preDanmuModel;
    }


    /**
     * 查找预制弹幕
     *
     * @return
     */
    public Object findPreDanmu(String addressId, Date nowDate, String partyId) {
        PartyLogicModel party = rpcPartyService.findPartyAddressId(addressId);
        //logger.info("预制弹幕获取：当前活动信息:{}", JSON.toJSONString(party));
        if (party != null) {
            partyId = party.getPartyId();
            //判断预制弹幕是否开启

            if (party.getType() == 0) {
                String key = FunctionControlCacheKey.FUNCITON_CONTROL_PREDANMU + partyId;
                if (redisService.get(key) == null) {
                    return null;
                }
            }
            logger.info("获取活动{}预制弹幕",party.getPartyId());
            Object object = redisService.popFromList(PreDanmuCacheKey.PARTY_PREDANMU_CACHE_LIST + party.getPartyId());
            if (object != null) {
                return object;
            }
        }
        return null;
    }

}
