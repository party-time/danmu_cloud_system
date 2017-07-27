package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcPromoService;
import cn.partytime.model.RestResultModel;

public class RpcPromoServiceHystrix implements RpcPromoService {
    @Override
    public RestResultModel sendPromoCommand(String name, String status, String registCode) {
        return null;
    }
}
