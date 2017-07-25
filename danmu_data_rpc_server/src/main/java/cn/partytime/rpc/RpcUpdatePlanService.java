package cn.partytime.rpc;

import cn.partytime.model.manager.UpdatePlan;
import cn.partytime.service.versionManager.UpdatePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */

@RestController
@RequestMapping("/rpcUpdatePlan")
public class RpcUpdatePlanService {

    @Autowired
    private UpdatePlanService updatePlanService;


    @RequestMapping(value = "/findByAddressId" ,method = RequestMethod.GET)
    public List<UpdatePlan> findByAddressId(@RequestParam String addressId){
        return updatePlanService.findByAddressId(addressId);
    }


    @RequestMapping(value = "/update" ,method = RequestMethod.GET)
    public void update(@RequestParam String id,@RequestParam Integer status,@RequestParam String machineNum){
        updatePlanService.update(id,status,machineNum);
    }
}
