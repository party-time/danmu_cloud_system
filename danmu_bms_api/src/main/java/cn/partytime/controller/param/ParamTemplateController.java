package cn.partytime.controller.param;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.ParamTemplate;
import cn.partytime.model.manager.ParamValue;
import cn.partytime.model.param.ObjectParamJson;
import cn.partytime.model.param.ParamTempJson;
import cn.partytime.service.param.BmsParamService;
import cn.partytime.service.param.ParamService;
import cn.partytime.service.param.ParamTemplateService;
import cn.partytime.service.param.ParamValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by administrator on 2017/2/23.
 */
@RestController
@RequestMapping(value = "/v1/api/admin/paramTemplate")
@Slf4j
public class ParamTemplateController {

    @Autowired
    private ParamTemplateService paramTemplateService;

    @Autowired
    private ParamService paramService;

    @Autowired
    private BmsParamService bmsParamService;

    @Autowired
    private ParamValueService paramValueService;



    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel paramTemplatePage(Integer pageNumber , Integer pageSize ){
        pageNumber = pageNumber-1;
        Page<ParamTemplate> paramTemplatePage = paramTemplateService.findAll(pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel(paramTemplatePage);
        return pageResultModel;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST,consumes = "application/json")
    public RestResultModel save(@RequestBody(required = false)ParamTempJson paramTempJson){
        RestResultModel restResultModel = new RestResultModel();
        bmsParamService.save(paramTempJson);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id){
        RestResultModel restResultModel = new RestResultModel();
        bmsParamService.del(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RestResultModel findById(String id){
        RestResultModel restResultModel = new RestResultModel();
        ParamTempJson paramTempJson = bmsParamService.findById(id);
        restResultModel.setResult(200);
        restResultModel.setData(paramTempJson);
        return restResultModel;
    }

    @RequestMapping(value = "/delParam", method = RequestMethod.GET)
    public RestResultModel delParam(String id){
        RestResultModel restResultModel = new RestResultModel();
        paramService.del(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public RestResultModel findAll(){
        RestResultModel restResultModel = new RestResultModel();
        List<ParamTemplate> paramTemplateList =paramTemplateService.findAll();
        restResultModel.setResult(200);
        restResultModel.setData(paramTemplateList);
        return restResultModel;
    }




    @RequestMapping(value = "/findByObj", method = RequestMethod.GET)
    public RestResultModel findByObjId(String objId,String paramTempId){
        RestResultModel restResultModel = new RestResultModel();
        List<ObjectParamJson> objectParamJsonList = bmsParamService.findByObjId(objId,paramTempId);
        restResultModel.setResult(200);
        restResultModel.setData(objectParamJsonList);
        return restResultModel;
    }


    @RequestMapping(value = "/updateParam", method = RequestMethod.POST,consumes = "application/json")
    public RestResultModel updateParam(@RequestBody(required = false)List<ParamValue> paramValueList){
        RestResultModel restResultModel = new RestResultModel();
        paramValueService.save(paramValueList);
        restResultModel.setResult(200);
        return restResultModel;
    }


}
