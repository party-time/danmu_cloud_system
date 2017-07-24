package cn.partytime.controller.param;

import cn.partytime.model.RestResultModel;
import cn.partytime.service.param.ParamValueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by administrator on 2017/2/23.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/paramValue")
@Slf4j
public class ParamValueController {

    @Autowired
    private ParamValueService paramValueService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel save(String objId,String paramId,String value){
        RestResultModel restResultModel = new RestResultModel();
        paramValueService.save(objId,paramId,value);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResultModel update(String id,String value){
        RestResultModel restResultModel = new RestResultModel();
        paramValueService.update(id,value);
        restResultModel.setResult(200);
        return restResultModel;
    }


}
