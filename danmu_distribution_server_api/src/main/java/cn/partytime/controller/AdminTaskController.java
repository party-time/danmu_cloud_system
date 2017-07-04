package cn.partytime.controller;

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

}
