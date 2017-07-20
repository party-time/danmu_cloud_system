package cn.partytime.rpcService.dataRpcService.impl;

import cn.partytime.rpcService.dataRpcService.UpdatePlanService;
import cn.partytime.model.UpdatePlan;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */

@Component
public class UpdatePlanServiceHystrix implements UpdatePlanService {
    @Override
    public List<UpdatePlan> findByAddressId(String addressId) {
        return null;
    }

    @Override
    public void update(String id, Integer status, String machineNum) {

    }
}
