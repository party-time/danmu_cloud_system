package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcDanmuClientService;
import cn.partytime.model.DanmuClientModel;

import java.util.List;

public class RpcDanmuClientServiceHystrix implements RpcDanmuClientService {
    @Override
    public DanmuClientModel findByRegistCode(String registCode) {
        return null;
    }

    @Override
    public List<DanmuClientModel> findByAddressId(String addressId) {
        return null;
    }
}
