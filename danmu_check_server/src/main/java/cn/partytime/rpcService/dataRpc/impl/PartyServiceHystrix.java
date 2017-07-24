package cn.partytime.rpcService.dataRpc.impl;

import cn.partytime.rpcService.dataRpc.PartyService;
import cn.partytime.model.Party;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/6.
 */

@Component
public class PartyServiceHystrix implements PartyService {
    @Override
    public Party findById(String partyId) {
        return null;
    }

    @Override
    public Party updateParty(Party party) {
        return null;
    }

}
