package cn.partytime.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dm on 2017/7/3.
 */
@RefreshScope
@RestController
public class TestController {

    @Value("${param}")
    String param ;

    @RequestMapping("/")
    public String home(){
        return "Hello " + param;
    }

}
