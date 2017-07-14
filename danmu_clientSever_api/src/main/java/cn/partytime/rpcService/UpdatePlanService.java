package cn.partytime.rpcService;

import cn.partytime.common.util.ServerConst;
import cn.partytime.rpcService.impl.UpdatePlanServiceHystrix;
import cn.partytime.model.UpdatePlan;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = UpdatePlanServiceHystrix.class)
public interface UpdatePlanService {

    @RequestMapping(value = "/rpcUpdatePlan/findByAddressId" ,method = RequestMethod.GET)
    public List<UpdatePlan> findByAddressId(@RequestParam(value = "addressId")String addressId);


    @RequestMapping(value = "/rpcUpdatePlan/update" ,method = RequestMethod.GET)
    public void update(@RequestParam(value = "id") String id,@RequestParam(value = "status")Integer status,@RequestParam(value = "machineNum") String machineNum);
}
