package cn.partytime.service;

import cn.partytime.cache.party.PartyCacheService;
import cn.partytime.common.cachekey.party.PartyCacheKey;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by dm on 2017/6/26.
 */


@Service
public class ClientPartyService {

    @Autowired
    private RpcPartyService rpcPartyService;


    @Autowired
    private PartyCacheService partyCacheService;


    public String findCurrentPatyId(String addressId){

        String partyId = partyCacheService.getCurrentPartyId(addressId);
        if(StringUtils.isEmpty(partyId)){
            PartyLogicModel partyLogicModel =  rpcPartyService.findPartyByAddressId(addressId);
            if(partyLogicModel!=null){
                return partyLogicModel.getPartyId();
            }

        }
        return partyId;
    }


}
