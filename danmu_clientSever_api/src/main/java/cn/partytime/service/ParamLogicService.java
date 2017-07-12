package cn.partytime.service;

import cn.partytime.model.ParamValueJson;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */

@Slf4j
@Service
public class ParamLogicService {

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
