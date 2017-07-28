package cn.partytime.service;

import cn.partytime.common.cachekey.PartyCacheKey;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.model.PartyLogicModel;
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
    private RpcPartyService rpcPartyService;


    public String findCurrentPatyId(String addressId){

        String key = PartyCacheKey.CURRENT_PARTY+addressId;
        Object object = redisService.get(key);
        if(object!=null){
            return String.valueOf(object);
        }else{
            PartyLogicModel partyLogicModel =  rpcPartyService.findPartyByAddressId(addressId);
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
