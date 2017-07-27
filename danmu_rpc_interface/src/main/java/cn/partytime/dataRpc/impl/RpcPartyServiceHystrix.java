package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.PartyModel;

import java.util.List;

public class RpcPartyServiceHystrix implements RpcPartyService {

    @Override
    public PartyLogicModel findPartyAddressId(String addressId) {
        return null;
    }

    @Override
    public PartyModel findByMovieAliasOnLine(String command) {
        return null;
    }

    @Override
    public PartyModel getPartyByPartyId(String partyId) {
        return null;
    }

    @Override
    public PartyLogicModel findTemporaryParty(String addressId) {
        return null;
    }

    @Override
    public PartyModel saveParty(PartyModel party) {
        return null;
    }

    @Override
    public List<String> findAddressIdListByPartyId(String partyId) {
        return null;
    }

    @Override
    public PartyModel getPartyId(String addressId) {
        return null;
    }

    @Override
    public PartyLogicModel findPartyByLonLat(Double longitude, Double latitude) {
        return null;
    }

    @Override
    public boolean checkPartyIsOver(PartyLogicModel party) {
        return false;
    }

    @Override
    public void deleteParty(String partyId) {

    }

    @Override
    public int getPartyDmDensity(String addressId, String partyId) {
        return 0;
    }

    @Override
    public List<PartyModel> findByTypeAndStatus(Integer type, Integer status) {
        return null;
    }

    @Override
    public List<PartyModel> findByAddressIdAndStatus(String addressId, Integer status) {
        return null;
    }
}
