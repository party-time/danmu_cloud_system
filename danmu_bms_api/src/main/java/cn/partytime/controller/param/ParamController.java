package cn.partytime.controller.param;

import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.Param;
import cn.partytime.service.param.ParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by administrator on 2017/2/23.
 */
@RestController
@RequestMapping(value = "/v1/api/admin/param")
@Slf4j
public class ParamController {

    @Autowired
    private ParamService paramService;


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel save(String name, Integer valueType, String paramTemplateId, String des, String defaultValue){
        RestResultModel restResultModel = new RestResultModel();
        paramService.save(name,valueType,paramTemplateId,des,defaultValue);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id){
        RestResultModel restResultModel = new RestResultModel();
        paramService.del(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RestResultModel findByParamTemplateId(String paramTemplateId){
        RestResultModel restResultModel = new RestResultModel();
        List<Param> paramList = paramService.findByParamTemplateId(paramTemplateId);
        restResultModel.setData(paramList);
        restResultModel.setResult(200);
        return restResultModel;
    }





}
