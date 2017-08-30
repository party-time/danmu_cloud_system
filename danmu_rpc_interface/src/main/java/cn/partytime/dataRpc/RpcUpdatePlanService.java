package cn.partytime.dataRpc;

import cn.partytime.dataRpc.impl.RpcUpdatePlanServiceHystrix;
import cn.partytime.model.JavaUpdatePlanModel;
import cn.partytime.model.UpdatePlanModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */

@FeignClient(value = "${dataRpcServer}",fallback = RpcUpdatePlanServiceHystrix.class)
public interface RpcUpdatePlanService {

    @RequestMapping(value = "/rpcUpdatePlan/findByAddressId" ,method = RequestMethod.GET)
    public List<JavaUpdatePlanModel> findByAddressId(@RequestParam(value = "addressId") String addressId);


    @RequestMapping(value = "/rpcUpdatePlan/update" ,method = RequestMethod.GET)
    public void update(@RequestParam(value = "id") String id, @RequestParam(value = "status") Integer status, @RequestParam(value = "machineNum") String machineNum);
}
