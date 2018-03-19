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
    public void insertOperationLog(String cmd, String partyId, String addressId, String adminUserId) {

    }

    @Override
    public void insertOperationLogOfParty(String cmd, String partyId, String addressId, String adminUserId, Map<String, Object> content) {

    }
}
