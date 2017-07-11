package cn.partytime.service.param;

import cn.partytime.model.client.DanmuClient;
import cn.partytime.model.manager.Param;
import cn.partytime.model.manager.ParamTemplate;
import cn.partytime.model.manager.ParamValue;
import cn.partytime.model.param.ObjectParamJson;
import cn.partytime.model.param.ParamTempJson;
import cn.partytime.model.param.ParamValueJson;
import cn.partytime.service.DanmuClientService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2017/3/10.
 */
@Slf4j
@Service
public class BmsParamService {

    @Autowired
    private ParamTemplateService paramTemplateService;

    @Autowired
    private ParamService paramService;

    @Autowired
    private ParamValueService paramValueService;

    @Autowired
    private DanmuClientService danmuClientService;

    public void save(ParamTempJson paramTempJson){
        if( null != paramTempJson){
            ParamTemplate paramTemplate = null;
            if( null != paramTempJson.getParamTempId()){
                paramTemplate = paramTemplateService.update(paramTempJson.getParamTempId(),paramTempJson.getParamTempName());
            }else{
                paramTemplate = paramTemplateService.save(paramTempJson.getParamTempName());
            }

            List<Param> paramList = paramTempJson.getParamList();
            if( null != paramList && paramList.size() >0){
                for(Param param : paramList){
                    param.setParamTemplateId(paramTemplate.getId());
                    paramService.save(param);
                }
            }
        }

    }

    public void del(String paramTemplateId){
        paramTemplateService.del(paramTemplateId);
        paramService.delByTemplateId(paramTemplateId);
    }

    public ParamTempJson findById(String paramTemplateId){
        ParamTempJson paramTempJson = new ParamTempJson();
        ParamTemplate paramTemplate = paramTemplateService.findById(paramTemplateId);
        paramTempJson.setParamTempName(paramTemplate.getName());

        List<Param> paramList = paramService.findByParamTemplateId(paramTemplateId);

        paramTempJson.setParamList(paramList);

        return paramTempJson;
    }

    public List<ObjectParamJson> findByObjId(String objId, String paramTemplateId){
        List<Param> paramList = paramService.findByParamTemplateId(paramTemplateId);
        List<String> paramIdList = new ArrayList<>();
        List<ObjectParamJson> objectParamJsonList = new ArrayList<>();
        if( null != paramList && paramList.size() >0){
            for(Param param : paramList){
                paramIdList.add(param.getId());
            }
            List<ParamValue> paramValueList = paramValueService.findByObjIdAndTypeAndParamIdList(objId,0,paramIdList);
            for(Param param : paramList){
                paramIdList.add(param.getId());
                ObjectParamJson objectParamJson = new ObjectParamJson();
                objectParamJson.setParamId(param.getId());
                objectParamJson.setDes(param.getDes());
                objectParamJson.setParamName(param.getName());
                objectParamJson.setValueType(param.getValueType());
                for(ParamValue paramValue : paramValueList){
                    if(param.getId().equals(paramValue.getParamId())){

                        objectParamJson.setParamValueId(paramValue.getId());
                        objectParamJson.setParamValue(paramValue.getValue());
                    }
                }
                if(StringUtils.isEmpty(objectParamJson.getParamValueId())){
                    objectParamJson.setParamValue(param.getDefaultValue());
                }

                objectParamJsonList.add(objectParamJson);
            }
        }
        return objectParamJsonList;
    }

    public List<ParamValueJson> findByRegistCode(String code){
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

    public String createJson(List<ParamValueJson> paramValueJsonList){
        if( null == paramValueJsonList){
            return null;
        }
        JSONObject jsonObject = new JSONObject(true);
        for(ParamValueJson paramValueJson : paramValueJsonList){
            if( !StringUtils.isEmpty(paramValueJson.getValue())){
                //0数字 1布尔值 2字符串 3数组
                if(paramValueJson.getType() == 0){
                    try {
                        jsonObject.put(paramValueJson.getName(), NumberFormat.getInstance().parse(paramValueJson.getValue()));
                    } catch (ParseException e) {
                        log.error("",e);
                    }
                }else if(paramValueJson.getType() == 1){
                    jsonObject.put(paramValueJson.getName(), Boolean.valueOf(paramValueJson.getValue()));
                }else if(paramValueJson.getType() == 2){
                    jsonObject.put(paramValueJson.getName(), paramValueJson.getValue());
                }else if(paramValueJson.getType() == 3){
                    String[] strings = paramValueJson.getValue().split(",");
                    jsonObject.put(paramValueJson.getName(),strings);
                }
            }else{
                jsonObject.put(paramValueJson.getName(),"");
            }
        }
        return jsonObject.toString();
    }


}
