package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcAdTimerService;
import cn.partytime.model.AdTimerResource;

import java.util.List;

public class RpcAdTimerServiceHystrix implements RpcAdTimerService {
    @Override
    public AdTimerResource findTimerDanmuFileList(String addressId, List<String> partyIdList) {
        return null;
    }
}
