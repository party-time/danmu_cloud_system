package cn.partytime.controller;

import cn.partytime.model.RestResultModel;
import cn.partytime.service.LogService;
import com.alibaba.fastjson.support.odps.udf.CodecCheck;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/3/18 0018.
 */

@RestController
@RequestMapping(value = "/log")
@Slf4j
public class LogController {

    private static final Logger logger = LoggerFactory.getLogger(LogController.class);
    @Autowired
    private LogService logService;

    @RequestMapping(value = "/flash", method = {RequestMethod.GET,RequestMethod.POST})
    public RestResultModel flash(HttpServletRequest request){
        RestResultModel restResultModel = new RestResultModel();
        //System.out.println("addressId:"+addressId+"; param:"+param);

        logService.appendLogToFile(1, request.getParameter("addressId"),request.getParameter("param"));
        return restResultModel;
    }

    @RequestMapping(value = "/java", method = {RequestMethod.GET,RequestMethod.POST})
    public RestResultModel javalog(HttpServletRequest request){
        RestResultModel restResultModel = new RestResultModel();
        logger.info("======================java log");
        logService.appendLogToFile(0, request.getParameter("addressId"),request.getParameter("param"));
        return restResultModel;
    }
}
