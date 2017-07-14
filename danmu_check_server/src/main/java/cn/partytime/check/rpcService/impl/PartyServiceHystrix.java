package cn.partytime.check.rpcService.impl;

import cn.partytime.check.rpcService.PartyService;
import cn.partytime.check.model.Party;
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
    public void updateParty(Party party) {

    }
}
