package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcPreDanmuService;
import cn.partytime.model.PreDanmuModel;

import java.util.List;

public class RpcPreDanmuServiceHystrix implements RpcPreDanmuService {
    @Override
    public List<PreDanmuModel> findByPartyId(String partyId) {
        return null;
    }
}
