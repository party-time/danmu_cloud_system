package cn.partytime.controller.danmu;


import cn.partytime.model.RestResultModel;
import cn.partytime.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jack on 15/6/26.
 */
@RestController
@RequestMapping(value = "/v1/api")
public class DanmuController{

    private static final Logger logger = LoggerFactory.getLogger(DanmuController.class);



    @Autowired
    private BmsDanmuService bmsDanmuService;






    @RequestMapping(value = "/wechartSend", method = RequestMethod.POST)
    public RestResultModel wechartSend(HttpServletRequest request) {
        logger.info("小程序端，弹幕发送");
        String openId = request.getParameter("openId");
        RestResultModel restResultModel = new RestResultModel();
        if (bmsDanmuService.checkFrequency(request)) {
            restResultModel.setResult(403);
            restResultModel.setResult_msg("Limited Frequency");
            logger.info("用户{}，发送弹幕,太频繁",openId);
            return restResultModel;
        }else if(bmsDanmuService.checkDanmuIsRepeat(openId,request.getParameter("message"))){
            restResultModel.setResult(403);
            restResultModel.setResult_msg("相同弹幕发送太多");
            logger.info("用户{}，相同弹幕发送太多",openId);
            return restResultModel;
        }else {
            return bmsDanmuService.sendDanmu(request,openId,0);
        }

    }

    @RequestMapping(value = "/sendExpression", method = RequestMethod.POST)
    public RestResultModel sendExpression(HttpServletRequest request) {
        logger.info("小程序端，发送表情");
        RestResultModel restResultModel = new RestResultModel();
        String openId = request.getParameter("openId");
        if (bmsDanmuService.checkFrequency(request)) {
            restResultModel.setResult(403);
            restResultModel.setResult_msg("Limited Frequency");
            logger.info("用户{}，发送弹幕,太频繁",openId);
            return restResultModel;
        }else {
            return  bmsDanmuService.sendDanmu(request,openId,0);
        }

    }


    @RequestMapping(value = "/danmu", method = RequestMethod.POST)
    public RestResultModel sendDanmu(HttpServletRequest request,@CookieValue(value = "openId") String openId) {
        RestResultModel restResultModel = new RestResultModel();
        if (bmsDanmuService.checkFrequency(request)) {
            restResultModel.setResult(403);
            restResultModel.setResult_msg("Limited Frequency");
            logger.info("用户{}，发送弹幕,太频繁",openId);
            return restResultModel;
        }else if(bmsDanmuService.checkDanmuIsRepeat(openId,request.getParameter("message"))){
            restResultModel.setResult(403);
            restResultModel.setResult_msg("相同弹幕发送太多");
            logger.info("用户{}，相同弹幕发送太多",openId);
            return restResultModel;
        }else {
            return bmsDanmuService.sendDanmu(request,openId,0);
        }
        /*if( StringUtils.isEmpty(color)){
            color = bmsDanmuService.getRandomColor();
        }
        logger.info("用户:{}，发送弹幕,消息内容:{},颜色:{}", openId, msg, color);
        msg = CommonUtils.filterEmoji(msg);

        //用户发送普通弹幕
        RestResultModel restResultModel = new RestResultModel();
        if (bmsDanmuService.checkFrequency(request)) {
            restResultModel.setResult(400);
            restResultModel.setResult_msg("Limited Frequency");
            logger.info("用户{}，发送弹幕,太频繁",openId);
            return restResultModel;
        }else if(bmsDanmuService.checkDanmuIsRepeat(openId,msg)){
            restResultModel.setResult(400);
            restResultModel.setResult_msg("相同弹幕发送太多");
            logger.info("用户{}，相同弹幕发送太多",openId);
            return restResultModel;
        }
        restResultModel = bmsDanmuService.saveDanmu(msg, color, openId, 1, 0);
        if (restResultModel != null) {
            return restResultModel;
        }
        restResultModel = new RestResultModel();
        restResultModel.setResult(500);
        restResultModel.setResult_msg("server err");
        return restResultModel;*/

    }



    @RequestMapping(value = "/expression", method = RequestMethod.POST)
    public RestResultModel sendExpression(String expression, HttpServletRequest request, @CookieValue(value = "openId") String openId) {
        RestResultModel restResultModel = new RestResultModel();
        logger.info("用户:{}，发送表情,消息内容:{},后缀:{}", openId, expression);
        if (bmsDanmuService.checkFrequency(request)) {
            restResultModel.setResult(403);
            restResultModel.setResult_msg("Limited Frequency");
            logger.info("用户{}，发送弹幕,太频繁",openId);
            return restResultModel;
        }else {
            return  bmsDanmuService.sendDanmu(request,openId,0);
        }

    }
}
