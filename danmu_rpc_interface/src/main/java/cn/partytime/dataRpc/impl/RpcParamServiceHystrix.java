package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcParamService;
import cn.partytime.model.ParamValueJsonModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RpcParamServiceHystrix implements RpcParamService {
    @Override
    public List<ParamValueJsonModel> findByRegistCode(String code) {
        return null;
    }
}
