package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcParamService;
import cn.partytime.model.ParamValueJsonModel;

import java.util.List;

public class RpcParamServiceHystrix implements RpcParamService {
    @Override
    public List<ParamValueJsonModel> findByRegistCode(String code) {
        return null;
    }
}
