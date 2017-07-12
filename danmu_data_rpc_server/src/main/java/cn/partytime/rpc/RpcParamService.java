package cn.partytime.rpc;

import cn.partytime.model.ParamValueJson;
import cn.partytime.model.client.DanmuClient;
import cn.partytime.model.manager.Param;
import cn.partytime.model.manager.ParamValue;
import cn.partytime.service.DanmuClientService;
import cn.partytime.service.param.ParamService;
import cn.partytime.service.param.ParamValueService;
import org.apache.commons.lang.StringUtils;
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
@RequestMapping("/rpcParamService")
public class RpcParamService {


    @Autowired
    private ParamService paramService;

    @Autowired
    private DanmuClientService danmuClientService;

    @Autowired
    private ParamValueService paramValueService;

    @RequestMapping(value = "/findByRegistCode" ,method = RequestMethod.GET)
    public List<ParamValueJson> findByRegistCode(@RequestParam String code){
        DanmuClient danmuClient = danmuClientService.findByRegistCode(code);
        if( null == danmuClient){
            return null;
        }
        List<Param> paramList = paramService.findByParamTemplateId(danmuClient.getParamTemplateId());
        List<String> paramIdList = new ArrayList<>();
        List<ParamValueJson> paramValueJsonList = new ArrayList<>();
        if( null != paramList && paramList.size() >0) {
            for (Param param : paramList) {
                paramIdList.add(param.getId());
            }
            List<ParamValue> paramValueList = paramValueService.findByObjIdAndTypeAndParamIdList(danmuClient.getId(), 0, paramIdList);
            for(Param param : paramList){
                ParamValueJson paramValueJson = new ParamValueJson();
                paramValueJson.setType(param.getValueType());
                paramValueJson.setName(param.getName());
                for(ParamValue paramValue : paramValueList){
                    if(param.getId().equals(paramValue.getParamId())){
                        paramValueJson.setValue(paramValue.getValue());
                    }
                }
                if(StringUtils.isEmpty(paramValueJson.getValue())){
                    paramValueJson.setValue(param.getDefaultValue());
                }
                paramValueJsonList.add(paramValueJson);
            }

        }
        return paramValueJsonList;
    }
}
