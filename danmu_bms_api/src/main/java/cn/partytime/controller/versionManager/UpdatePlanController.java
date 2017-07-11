package cn.partytime.controller.versionManager;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.UpdatePlan;
import cn.partytime.model.manager.Version;
import cn.partytime.model.versionManager.UpdatePlanResult;
import cn.partytime.service.versionManager.BmsUpdatePlanService;
import cn.partytime.service.versionManager.UpdatePlanService;
import cn.partytime.service.versionManager.VersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 2017/2/13.
 */
@RestController
@RequestMapping(value = "/v1/api/admin/updateplan")
@Slf4j
public class UpdatePlanController {

    @Autowired
    private UpdatePlanService updatePlanService;

    @Autowired
    private BmsUpdatePlanService bmsUpdatePlanService;

    @Autowired
    private VersionService versionService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel<UpdatePlanResult> findAll(String addressId , Integer pageSize, Integer pageNumber){
        pageNumber = pageNumber-1;
        return bmsUpdatePlanService.findByAddressId(addressId,pageSize,pageNumber);
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public RestResultModel save(String addressId,String versionId,String updateTimeStr){
        RestResultModel restResultModel = new RestResultModel();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date updateTime = null;
        try {
            updateTime = dateFormat.parse(updateTimeStr);
        } catch (ParseException e) {
            restResultModel.setResult(501);
            restResultModel.setResult_msg("更新时间格式错误");
            return restResultModel;
        }
        //需要优化性能
        List<UpdatePlan> updatePlanList = updatePlanService.findByAddressIdAndStatusNot(addressId,1);
        Version version = versionService.findById(versionId);
        if( null != updatePlanList && updatePlanList.size() > 0){
            for(UpdatePlan updatePlan : updatePlanList){
                Version version1 = versionService.findById(updatePlan.getVersionId());
                if( version.getType() == version1.getType() ){
                    restResultModel.setResult(501);
                    restResultModel.setResult_msg("本场地还有未执行的升级计划，请升级完成或者删除未执行的升级计划");
                    return restResultModel;
                }
            }
        }
        updatePlanService.save(addressId,versionId,updateTime);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel save(String id){
        RestResultModel restResultModel = new RestResultModel();
        updatePlanService.del(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

}
