package cn.partytime.rpcService.dataRpc.impl;
import cn.partytime.model.PartyDTO;
import cn.partytime.rpcService.dataRpc.PartyLogicService;
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
    public PartyDTO getPartyId(String addressId) {
        return null;
    }
}
