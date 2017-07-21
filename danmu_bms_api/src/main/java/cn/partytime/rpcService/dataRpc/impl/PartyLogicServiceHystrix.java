package cn.partytime.rpcService.dataRpc.impl;

import cn.partytime.rpcService.PartyLogicService;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.manager.Party;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
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

    @Override
    public PartyLogicModel findPartyAddressId(String addressId) {
        return null;
    }

    @Override
    public PartyLogicModel findPartyByLonLat(Double longitude, Double latitude) {
        return null;
    }

    @Override
    public PartyLogicModel findTemporaryParty(String addressId) {
        return null;
    }

    @Override
    public boolean checkPartyIsOver(Party party) {
        return false;
    }

    @Override
    public void deleteParty(String partyId) {

    }
}
