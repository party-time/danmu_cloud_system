package cn.partytime.collector.service;

import cn.partytime.collector.rpcService.PartyService;
import cn.partytime.collector.model.PartyLogicModel;
import cn.partytime.common.cachekey.PartyCacheKey;
import cn.partytime.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dm on 2017/6/26.
 */


@Service
public class ClientPartyService {


    @Autowired
    private RedisService redisService;

    @Autowired
    private PartyService partyService;


    public String findCurrentPatyId(String addressId){

        String key = PartyCacheKey.CURRENT_PARTY+addressId;
        Object object = redisService.get(key);
        if(object!=null){
            return String.valueOf(object);
        }else{
            PartyLogicModel partyLogicModel =  partyService.findPartyAddressId(addressId);
            if(partyLogicModel!=null){
                String partyId = partyLogicModel.getPartyId();
                redisService.set(key,partyId);
                redisService.expire(key,30);
                return partyId;
            }
        }
        return "";

    }


}
