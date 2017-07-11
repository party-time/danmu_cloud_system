package cn.partytime.filter;

import cn.partytime.common.constants.CommonConst;
import cn.partytime.controller.base.BaseAdminController;
import cn.partytime.model.manager.AdminUser;
import cn.partytime.service.AdminUserService;
import cn.partytime.service.BmsAdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by administrator on 2016/11/24.
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private BmsAdminUserService bmsAdminUserService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if( o instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) o;
            String url = request.getRequestURI();
            String cookieValue = "";
            //判断是否需要登录验证
            if( url.indexOf("/v1/api/admin") != -1 || url.equals("/v1/logout")){
                Cookie[] cookies =request.getCookies();
                if( null != cookies) {
                    for (Cookie cookie : cookies) {
                        if (CommonConst.COOKIE_AUTH_KEY.equals(cookie.getName())) {
                            cookieValue = cookie.getValue();
                        }
                    }
                    //对auth_key进行校验
                    if (!StringUtils.isEmpty(cookieValue)) {
                        if (!adminUserService.checkAuthKey(cookieValue)) {
                            response.setStatus(444);
                            return false;
                        } else {
                            //每次操作后增加session时间
                            bmsAdminUserService.addSessionTime(cookieValue, response);
                        }
                    } else {
                        response.setStatus(444);
                        return false;
                    }
                }else{
                    response.setStatus(444);
                    return false;
                }
            }
            AdminUser adminUser = adminUserService.getAdminUser(cookieValue);
            //注入adminUser
            if( handlerMethod.getBean() instanceof BaseAdminController){
                BaseAdminController baseAdminController = (BaseAdminController) handlerMethod.getBean();
                baseAdminController.setAdminUser(adminUser);
            }
            if( null != adminUser){
                if(!StringUtils.isEmpty(adminUser.getRoleId()) && adminUser.getRoleId().equals("589a98cd77c8afdcbdeaeeb6")){
                    if( url.indexOf("/film/danmuCheck") ==-1 && !url.equals("/v1/logout") ){
                        response.setStatus(443);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
