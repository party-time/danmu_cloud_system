package cn.partytime.rpcService.dataRpc.impl;

import cn.partytime.model.PartyDTO;
import cn.partytime.rpcService.dataRpc.PartyService;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/6.
 */

@Component
public class PartyServiceHystrix implements PartyService {
    @Override
    public PartyDTO findById(String partyId) {
        return null;
    }

    @Override
    public PartyDTO updateParty(PartyDTO party) {
        return null;
    }

}
