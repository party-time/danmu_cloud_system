package cn.partytime.controller.projector;

import cn.partytime.common.util.DateUtils;
import cn.partytime.controller.base.BaseAdminController;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.projector.Projector;
import cn.partytime.service.projector.ProjectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by dm on 2017/6/14.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/projector")
public class ProjectorController extends BaseAdminController {


    @Autowired
    private ProjectorService projectorService;

    @RequestMapping(value = "/info/{registerCode}", method = RequestMethod.GET)
    public RestResultModel info(@PathVariable("registerCode") String registerCode){
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        restResultModel.setData(projectorService.findByRegisterCode(registerCode));
        return restResultModel;
    }


    @RequestMapping(value = "/setRealHours", method = RequestMethod.GET)
    public RestResultModel setRealHours(String registerCode,Integer realUsedHours,Integer realUsedMinute){
        RestResultModel restResultModel = new RestResultModel();
        String userId = getAdminUser().getId();
        Date date = DateUtils.getCurrentDate();
        Projector projector = projectorService.findByRegisterCode(registerCode);
        if(projector!=null){
            projector.setRealUsedHours(realUsedHours);
            projector.setRealUsedMinute(realUsedMinute);
            projector.setUsedTime(realUsedHours*60*60+realUsedMinute*60);
            projector.setUpdateTime(date);
            projector.setUpdateUserId(userId);
        }else{
            projector = new Projector();
            projector.setRegisterCode(registerCode);
            projector.setRealUsedHours(realUsedHours);
            projector.setRealUsedMinute(realUsedMinute);
            projector.setUsedTime(realUsedHours*60*60+realUsedMinute*60);
            projector.setCreateTime(date);
            projector.setUpdateTime(date);
            projector.setCreateUserId(userId);
            projector.setUpdateUserId(userId);
        }
        projectorService.save(projector);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public RestResultModel reset(String registerCode){
        RestResultModel restResultModel = new RestResultModel();
        String userId = getAdminUser().getId();
        Date date = DateUtils.getCurrentDate();
        Projector projector = projectorService.findByRegisterCode(registerCode);
        if(projector!=null){
            projector.setRealUsedHours(0);
            projector.setUsedTime(0);
            projector.setStartTime(date);
            projector.setUpdateTime(date);
            projector.setUpdateUserId(userId);
        }else{
            projector = new Projector();
            projector.setRegisterCode(registerCode);
            projector.setRealUsedHours(0);
            projector.setUsedTime(0);
            projector.setStartTime(date);
            projector.setCreateTime(date);
            projector.setUpdateTime(date);
            projector.setCreateUserId(userId);
            projector.setUpdateUserId(userId);
        }
        projectorService.save(projector);
        restResultModel.setResult(200);
        return restResultModel;
    }

}
