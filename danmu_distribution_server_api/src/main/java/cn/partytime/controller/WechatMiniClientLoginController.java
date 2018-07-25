package cn.partytime.controller;


import cn.partytime.cache.wechatmin.WechatMiniCacheService;
import cn.partytime.logicService.WechatMiniClientLoginService;
import cn.partytime.model.ResultInfo;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "/distribute/wechatMini")
public class WechatMiniClientLoginController {


    @Autowired
    private WechatMiniClientLoginService wechatMiniClientLoginService;

    @RequestMapping(value = "/login/{userKey}" ,method = RequestMethod.GET)
    public ResultInfo view(HttpServletRequest request, @PathVariable("userKey") String userKey) {

        return wechatMiniClientLoginService.findCollectorInfo(userKey);
    }

}
