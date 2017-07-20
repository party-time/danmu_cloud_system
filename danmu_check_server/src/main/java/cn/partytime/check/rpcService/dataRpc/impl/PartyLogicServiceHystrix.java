package cn.partytime.check.rpcService.dataRpc.impl;
import cn.partytime.check.rpcService.dataRpc.PartyLogicService;
import cn.partytime.check.model.Party;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dm on 2017/7/6.
 */

@Component
public class PartyLogicServiceHystrix implements PartyLogicService {

    @Override
    public List<String> findAddressIdListByPartyId(String partyId) {
        return null;
    }

    @Override
    public Party getPartyId(String addressId) {
        return null;
    }
}
