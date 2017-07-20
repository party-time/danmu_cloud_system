package cn.partytime.rpcService.dataRpcService.impl;

import cn.partytime.rpcService.dataRpcService.PartyResourceResultService;
import cn.partytime.model.PartyResourceResult;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */

@Component
public class PartyResourceResultServiceHystrix implements PartyResourceResultService {
    @Override
    public List<PartyResourceResult> findLatelyParty() {
        return null;
    }

    @Override
    public List<PartyResourceResult> findLatelyPartyByAddressIdAndType(String addressId, Integer type) {
        return null;
    }
}
