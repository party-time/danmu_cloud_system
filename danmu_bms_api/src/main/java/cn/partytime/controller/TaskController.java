package cn.partytime.controller;

import cn.partytime.model.RestResultModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by administrator on 2017/12/18.
 */
@RestController
@RequestMapping(value = "/v1/api/admin/task")
public class TaskController {




    @RequestMapping(value = "/taskController", method = RequestMethod.GET)
    public RestResultModel del(String taskName) {
        RestResultModel restResultModel =new RestResultModel();

        restResultModel.setResult(200);
        return restResultModel;
    }
}
