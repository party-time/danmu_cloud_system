package cn.partytime.rpcService.dataRpc.impl;

import cn.partytime.rpcService.dataRpc.PartyService;
import cn.partytime.model.PartyLogicModel;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/6.
 */

@Component
public class PartyServiceHystrix implements PartyService {
    @Override
    public PartyLogicModel findPartyByLonLat(Double longitude, Double latitude) {
        return null;
    }

    @Override
    public PartyLogicModel findPartyAddressId(String addressId) {
        return null;
    }

    @Override
    public int getPartyDmDensity(String addressId, String partyId) {
        return 0;
    }
}
