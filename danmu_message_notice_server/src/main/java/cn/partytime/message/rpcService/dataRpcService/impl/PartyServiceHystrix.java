package cn.partytime.message.rpcService.dataRpcService.impl;

import cn.partytime.message.model.Party;
import cn.partytime.message.model.PartyLogicModel;
import cn.partytime.message.rpcService.dataRpcService.PartyService;
import org.springframework.stereotype.Service;


@Service
public class PartyServiceHystrix implements PartyService {
    @Override
    public PartyLogicModel findPartyAddressId(String addressId) {
        return null;
    }

    @Override
    public Party getPartyByPartyId(String partyId) {
        return null;
    }
}
