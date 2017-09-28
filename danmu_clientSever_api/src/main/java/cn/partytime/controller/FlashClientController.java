package cn.partytime.controller;

import cn.partytime.model.RestResultModel;
import cn.partytime.service.FlashConfigService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by administrator on 2017/9/5.
 */
@RestController
@RequestMapping(value = "/v1/api/javaClient")
@Slf4j
public class FlashClientController {

    @Autowired
    private FlashConfigService flashConfigService;


    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public String config(String code){
        String jsonStr = null;
        try{
            jsonStr = flashConfigService.createConfig(code);
        }catch (Exception e){
            log.error("",e);
        }

        if(StringUtils.isEmpty(jsonStr)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result",500);
            return jsonObject.toJSONString();
        }else{
            return jsonStr;
        }


    }

}
