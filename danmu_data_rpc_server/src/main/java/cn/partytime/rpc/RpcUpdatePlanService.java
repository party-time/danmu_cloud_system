package cn.partytime.rpc;

import cn.partytime.model.JavaUpdatePlanModel;
import cn.partytime.model.manager.UpdatePlan;
import cn.partytime.model.manager.Version;
import cn.partytime.service.versionManager.UpdatePlanService;
import cn.partytime.service.versionManager.VersionService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */

@RestController
@RequestMapping("/rpcUpdatePlan")
@Slf4j
public class RpcUpdatePlanService {

    @Autowired
    private UpdatePlanService updatePlanService;

    @Autowired
    private VersionService versionService;


    @RequestMapping(value = "/findByAddressId" ,method = RequestMethod.GET)
    public List<JavaUpdatePlanModel> findByAddressId(@RequestParam String addressId){
        //return updatePlanService.findByAddressId(addressId);
        List<UpdatePlan> updatePlanList = updatePlanService.findByAddressIdAndStatus(addressId,0);
        log.info("addressId:{}",addressId);
        log.info("updatePlanList:{}",JSON.toJSONString(updatePlanList));

        List<JavaUpdatePlanModel> javaUpdatePlanResults = new ArrayList<>();
        if( null != updatePlanList){
            for(UpdatePlan updatePlan : updatePlanList){
                log.info("updatePlan:{}", JSON.toJSONString(updatePlan));
                JavaUpdatePlanModel javaUpdatePlanResult = new JavaUpdatePlanModel();
                javaUpdatePlanResult.setId(updatePlan.getId());
                Version version = versionService.findById(updatePlan.getVersionId());
                javaUpdatePlanResult.setType(version.getType());
                javaUpdatePlanResult.setVersion(version.getVersion());
                javaUpdatePlanResult.setUpdateDate(updatePlan.getUpdatePlanTime());
                javaUpdatePlanResults.add(javaUpdatePlanResult);
            }
        }

        return javaUpdatePlanResults;
    }


    @RequestMapping(value = "/update" ,method = RequestMethod.GET)
    public void update(@RequestParam String id,@RequestParam Integer status,@RequestParam String machineNum){
        updatePlanService.update(id,status,machineNum);
    }
}
