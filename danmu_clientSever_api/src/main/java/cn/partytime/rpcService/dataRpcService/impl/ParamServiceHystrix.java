package cn.partytime.rpcService.dataRpcService.impl;

import cn.partytime.rpcService.dataRpcService.ParamService;
import cn.partytime.model.ParamValueJson;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */
@Component
public class ParamServiceHystrix implements ParamService {
    @Override
    public List<ParamValueJson> findByRegistCode(String code) {
        return null;
    }
}
