package cn.partytime.controller;

import cn.partytime.model.ResourceConfigJson;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.client.DanmuClient;
import cn.partytime.service.DanmuClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuwei on 2016/9/7.
 */

@RestController
@RequestMapping(value = "/v1/api/flash")
@Slf4j
public class FlashController {

    @Autowired
    private DanmuClientService danmuClientService;

    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public RestResultModel verifyDanmuClient(String danmuClientCode){
        RestResultModel restResultModel = new RestResultModel();
        DanmuClient danmuClient = danmuClientService.verifyDanmuClient(danmuClientCode);
        if( null == danmuClient){
            restResultModel.setResult(404);
            restResultModel.setResult_msg("验证不通过");
        }
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/regist", method = RequestMethod.GET)
    public RestResultModel regist(String danmuClientCode, String registCode){
        RestResultModel restResultModel = new RestResultModel();
        try {
            danmuClientService.registClient(danmuClientCode, registCode);
        }catch (Exception e){
            restResultModel.setResult(500);
            restResultModel.setResult_msg(e.getMessage());
            return restResultModel;
        }
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public ResourceConfigJson getFlashConfig(String clientId){
        ResourceConfigJson resourceConfigJson = new ResourceConfigJson();


        return resourceConfigJson;
    }


}
