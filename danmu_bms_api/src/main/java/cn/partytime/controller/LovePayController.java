package cn.partytime.controller;

import cn.partytime.model.PageResultModel;
import cn.partytime.service.BmsLovePayservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by administrator on 2017/7/25.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/lovePay")
@Slf4j
public class LovePayController {

    @Autowired
    private BmsLovePayservice bmsLovePayservice;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel findAll(Integer pageNumber , Integer pageSize  ){

        pageNumber = pageNumber-1;

        return bmsLovePayservice.findAll(pageNumber,pageSize);
    }


}
