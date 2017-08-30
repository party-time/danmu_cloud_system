package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcUpdatePlanService;
import cn.partytime.model.JavaUpdatePlanModel;
import cn.partytime.model.UpdatePlanModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RpcUpdatePlanServiceHystrix implements RpcUpdatePlanService {

    @Override
    public List<JavaUpdatePlanModel> findByAddressId(String addressId) {
        return null;
    }

    @Override
    public void update(String id, Integer status, String machineNum) {

    }
}
