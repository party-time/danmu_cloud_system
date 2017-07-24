package cn.partytime.controller.login;

import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.AdminUser;
import cn.partytime.service.AdminUserService;
import cn.partytime.service.BmsAdminUserService;
import cn.partytime.common.constants.CommonConst;
import cn.partytime.service.BmsWechatUserService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by administrator on 2016/11/8.
 */
@RestController
@Slf4j
public class loginController {

    @Autowired
    private BmsAdminUserService bmsAdminUserService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private BmsWechatUserService bmsWechatUserService;

    @RequestMapping(value = "/v1/login" , method = RequestMethod.POST)
    public RestResultModel login(String name,String password,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
        RestResultModel restResultModel = new RestResultModel();
        if( StringUtils.isEmpty(name) || StringUtils.isEmpty(password)){
            restResultModel.setResult(503);
            restResultModel.setResult_msg("用户名或者密码不能为空");
            return restResultModel;
        }
        String clientIp = request.getHeader("x-forwarded-for");
        if(StringUtils.isEmpty(clientIp)){
            clientIp = request.getRemoteAddr();
        }
        if(!StringUtils.isEmpty(clientIp) && clientIp.indexOf(",")!=-1){
            clientIp = clientIp.substring(0,clientIp.indexOf(","));
        }
        AdminUser adminUser = bmsAdminUserService.login(name,password,clientIp,response);
        if( null == adminUser){
            restResultModel.setResult(404);
            restResultModel.setResult_msg("用户名或者密码错误");
            return restResultModel;
        }else{
            restResultModel.setResult(200);

        }
        return restResultModel;
    }

    @RequestMapping(value = "/v1/loginByWechat" , method = RequestMethod.POST)
    public RestResultModel loginByWechat(String name,String code,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
        RestResultModel restResultModel = new RestResultModel();
        if( StringUtils.isEmpty(name) || StringUtils.isEmpty(code)){
            restResultModel.setResult(503);
            restResultModel.setResult_msg("用户名或者验证码不能为空");
            return restResultModel;
        }
        String clientIp = request.getHeader("x-forwarded-for");
        if(StringUtils.isEmpty(clientIp)){
            clientIp = request.getRemoteAddr();
        }
        if(!StringUtils.isEmpty(clientIp) && clientIp.indexOf(",")!=-1){
            clientIp = clientIp.substring(0,clientIp.indexOf(","));
        }
        AdminUser adminUser = bmsAdminUserService.loginByWechatCode(name,code,clientIp,response);
        if( null == adminUser){
            restResultModel.setResult(404);
            restResultModel.setResult_msg("用户名或者验证码错误");
            return restResultModel;
        }else{
            restResultModel.setResult(200);

        }
        return restResultModel;
    }

    @RequestMapping(value = "/v1/logout" , method = RequestMethod.GET)
    public RestResultModel logout(HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
        RestResultModel restResultModel = new RestResultModel();

        bmsAdminUserService.logout(request);

        Cookie cookie = new Cookie(CommonConst.COOKIE_AUTH_KEY,"");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        Cookie cookieRole = new Cookie(CommonConst.COOKIE_ROLE,"");
        cookieRole.setMaxAge(0);
        cookieRole.setPath("/");
        response.addCookie(cookieRole);

        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/v1/getWeChatCode" , method = RequestMethod.GET)
    public RestResultModel getWeChatCode(String userName,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException {
        RestResultModel restResultModel = new RestResultModel();
        if(StringUtils.isEmpty(userName)){
            restResultModel.setResult(500);
            restResultModel.setResult_msg("请填写用户名");
            return restResultModel;
        }
        String openId = bmsAdminUserService.findOpenIdByUserName(userName);
        if( null != openId){
            String result = bmsWechatUserService.sendLoginTmpl(openId);
            if( null == result ){
                restResultModel.setResult(200);
            }else{
                restResultModel.setResult(500);
                restResultModel.setResult_msg(result);
            }
        }

        return restResultModel;
    }
}
