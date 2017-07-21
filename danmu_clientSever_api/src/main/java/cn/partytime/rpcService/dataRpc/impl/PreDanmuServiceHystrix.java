package cn.partytime.rpcService.dataRpc.impl;

import cn.partytime.model.PreDanmuModel;
import cn.partytime.rpcService.dataRpc.PreDanmuService;

import java.util.List;

public class PreDanmuServiceHystrix implements PreDanmuService {
    @Override
    public List<PreDanmuModel> findByPartyId(String partyId) {
        return null;
    }
}
