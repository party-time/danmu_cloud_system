package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcPartyResourceResultService;
import cn.partytime.model.PartyResourceResult;
import cn.partytime.model.ResourceFileModel;

import java.util.List;
import java.util.Map;

public class RpcPartyResourceResultServiceHystrix implements RpcPartyResourceResultService {

    @Override
    public Map<String, List<ResourceFileModel>> findResourceUnderFilm(List<String> partyIdList) {
        return null;
    }

    @Override
    public Map<String, List<ResourceFileModel>> findResourceUnderParty(String addressId, Integer type, List<String> partyIdList) {
        return null;
    }
}
