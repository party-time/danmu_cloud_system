package cn.partytime.controller;

import cn.partytime.bean.DanmuClient;
import cn.partytime.dataService.DanmuClientService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by task on 2016/6/21.
 */

@RestController
@RequestMapping(value = "/distribute/client")
public class ClientLoginController {


    private static final Logger logger = LoggerFactory.getLogger(ClientLoginController.class);

    @Autowired
    private DanmuClientService danmuClientService;

    /*@RequestMapping("/login/{code}")
    public Integer  view(@PathVariable("code") String code) {
        return danmuClientService.add(1,1);
    }*/

    @RequestMapping("/login/{code}")
    public DanmuClient findDanmuClient(@PathVariable("code") String code){
        return danmuClientService.findByRegistCode(code);
    }


}
