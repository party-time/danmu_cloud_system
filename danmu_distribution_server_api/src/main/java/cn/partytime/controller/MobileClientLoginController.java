package cn.partytime.controller;

import cn.partytime.logicService.MobileLoginService;
import cn.partytime.model.ResultInfo;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lENOVO on 2016/9/23.
 */


@RestController
@RequestMapping(value = "/distribute/mobile/client")
public class MobileClientLoginController {

    @Autowired
    private MobileLoginService mobileLoginService;

    @RequestMapping(value = "/login/{code}" ,method = RequestMethod.GET)
    public String view(HttpServletRequest request, @PathVariable("code") String openId) {
        //return clientLogicService.findDanmuServerInfo(code);
        ResultInfo resultInfo = mobileLoginService.findCollectorInfo(openId);
        if (resultInfo != null) {
            return JSON.toJSONString(resultInfo);
        }
        return null;
    }
}
