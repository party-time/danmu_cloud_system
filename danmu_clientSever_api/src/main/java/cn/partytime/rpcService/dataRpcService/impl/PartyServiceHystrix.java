package cn.partytime.rpcService.dataRpcService.impl;

import cn.partytime.model.Party;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.rpcService.dataRpcService.PartyService;
import org.springframework.stereotype.Service;

/**
 * Created by dm on 2017/7/19.
 */

@Service
public class PartyServiceHystrix implements PartyService {

    @Override
    public PartyLogicModel findPartyAddressId(String addressId) {
        return null;
    }

    @Override
    public Party findByMovieAliasOnLine(String command) {
        return null;
    }

    @Override
    public Party getPartyByPartyId(String partyId) {
        return null;
    }
}
