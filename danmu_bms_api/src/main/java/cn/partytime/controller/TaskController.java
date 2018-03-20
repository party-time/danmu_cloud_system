package cn.partytime.controller;

import cn.partytime.model.RestResultModel;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by administrator on 2017/12/18.
 */
@RestController
@RequestMapping(value = "/v1/api/admin/task")
public class TaskController {


    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/taskController", method = RequestMethod.GET)
    public RestResultModel del(String taskName) {
        RestResultModel restResultModel =new RestResultModel();
        System.out.println("taskName--------------------:"+taskName);

        redisTemplate.convertAndSend("secheduler:command:key", taskName);

        restResultModel.setResult(200);
        return restResultModel;
    }
}
