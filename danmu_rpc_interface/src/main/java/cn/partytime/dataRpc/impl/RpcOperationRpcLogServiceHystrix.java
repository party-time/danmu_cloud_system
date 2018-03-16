package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcOperationRpcLogService;
import cn.partytime.model.ParamValueJsonModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2018/3/16.
 */
@Component
public class RpcOperationRpcLogServiceHystrix implements RpcOperationRpcLogService {

    @Override
    public List<ParamValueJsonModel> insertOperationLog(String code, Map<String, String> content, String adminUserId) {
        return null;
    }
}
