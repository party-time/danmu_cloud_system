package cn.partytime.controller;

import cn.partytime.model.ResultInfo;
import cn.partytime.service.ClientLogicService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by task on 2016/6/21.
 */

@RestController
@RequestMapping(value = "/distribute/client")
public class ClientLoginController {


    private static final Logger logger = LoggerFactory.getLogger(ClientLoginController.class);

    @Resource(name = "clientLogicService")
    private ClientLogicService clientLogicService;

    @RequestMapping("/login/{code}")
    public String view(@PathVariable("code") String code) {
        //logger.info("客户端屏幕登录");

        try{
            ResultInfo resultInfo = clientLogicService.findCollectorInfo(code);
            if (resultInfo != null) {
                return JSON.toJSONString(resultInfo);
            }
        }catch (Exception e){
            logger.error("",e);
               return null;
        }
        return null;
    }
}
