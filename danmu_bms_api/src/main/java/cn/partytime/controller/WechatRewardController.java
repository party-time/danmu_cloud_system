package cn.partytime.controller;

import cn.partytime.logic.danmu.PageResultModel;
import cn.partytime.service.BmsWechatRewardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by administrator on 2016/11/29.
 */
@RestController
@RequestMapping(value = "/v1/api/admin/reward")
@Slf4j
public class WechatRewardController {

    @Autowired
    private BmsWechatRewardService bmsWechatRewardService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel findAll(Integer pageNumber,Integer pageSize){
        pageNumber = pageNumber-1;
        return bmsWechatRewardService.findAll(pageNumber,pageSize);

    }



}
