package cn.partytime.controller;

import cn.partytime.logicService.AdminTaskService;
import cn.partytime.model.ResultInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lENOVO on 2016/11/22.
 */

@RestController
@RequestMapping(value = "/distribute/adminTask")
public class AdminTaskController {

    private static final Logger logger = LoggerFactory.getLogger(AdminTaskController.class);

    @Autowired
    private AdminTaskService adminTaskService;

    @RequestMapping(value = "/socketAddress", method = RequestMethod.GET)
    public ResultInfo socketAddress(HttpServletRequest request) {
        logger.info("获取task socketAddress连接地址");
        return adminTaskService.findTaskServerInfo();
    }

    @RequestMapping(value = "/filmSocketAddress", method = RequestMethod.GET)
    public ResultInfo filmSocketAddress(HttpServletRequest request) {
        logger.info("获取task socketAddress连接地址");
        return adminTaskService.findFilmTaskServerInfo();
    }

}
