package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcPreDanmuService;
import cn.partytime.model.PreDanmuModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RpcPreDanmuServiceHystrix implements RpcPreDanmuService {
    @Override
    public List<PreDanmuModel> findByPartyId(String partyId) {
        return null;
    }
}
