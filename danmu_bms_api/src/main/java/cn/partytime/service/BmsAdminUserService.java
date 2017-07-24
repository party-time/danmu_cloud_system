package cn.partytime.service;

import cn.partytime.common.cachekey.AdminUserCacheKey;
import cn.partytime.common.cachekey.LoginCodeCacheKey;
import cn.partytime.model.manager.AdminRole;
import cn.partytime.model.manager.AdminUser;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.redis.service.RedisService;
import cn.partytime.service.wechat.WechatUserService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.partytime.common.constants.CommonConst;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by administrator on 2016/11/3.
 */
@Service
public class BmsAdminUserService {

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private AdminRoleService adminRoleService;


    /**
     * 管理员登陆
     * @param userName
     * @param password
     * @param ip
     * @return
     */
    public AdminUser login(String userName,String password,String ip,HttpServletResponse response) throws UnsupportedEncodingException {
        AdminUser adminUser = adminUserService.findByUserName(userName);
        if( null ==adminUser){
            return null;
        }
        if( !BCrypt.checkpw(password,adminUser.getPassword())){
            return null;
        }
        adminUser.setLastLoginIp(ip);
        adminUser.setLastLoginTime(new Date());
        adminUserService.updateLoginInfoById(adminUser);

        String key = adminUserService.createAuthKey(adminUser);
        Cookie authKeyCookie = new Cookie(CommonConst.COOKIE_AUTH_KEY, key);
        authKeyCookie.setMaxAge(3600*3);
        authKeyCookie.setPath("/");

        Cookie nickCookie = new Cookie(CommonConst.COOKIE_NICK, URLEncoder.encode(adminUser.getNick(), "UTF-8") );
        nickCookie.setMaxAge(3600*24*30);
        nickCookie.setPath("/");

        response.addCookie(authKeyCookie);
        response.addCookie(nickCookie);

        if(!StringUtils.isEmpty(adminUser.getRoleId())) {
            Cookie roleCookie = new Cookie(CommonConst.COOKIE_ROLE, URLEncoder.encode(adminUser.getRoleId(), "UTF-8"));
            roleCookie.setMaxAge(3600 * 24 * 30);
            roleCookie.setPath("/");
            response.addCookie(roleCookie);
        }

        return adminUser;
    }



    public void logout(HttpServletRequest request){

        Cookie[] cookies = request.getCookies();
        if( null != cookies && cookies.length > 0){
            for(Cookie cookie : cookies){
                if(CommonConst.COOKIE_AUTH_KEY.equals(cookie.getName())){
                    redisService.deleteByKey(AdminUserCacheKey.ADMIN_USER_CACHE_KEY+cookie.getValue());

                }
            }
        }

    }


    public String findOpenIdByUserName(String userName){
        AdminUser adminUser = adminUserService.findByUserName(userName);
        if( null != adminUser && !StringUtils.isEmpty(adminUser.getWechatId()) && !StringUtils.isEmpty(adminUser.getRoleId())){
            WechatUser wechatUser = wechatUserService.findById(adminUser.getWechatId());
            AdminRole adminRole = adminRoleService.findById(adminUser.getRoleId());
            if( null ==adminRole){
                return null;
            }else if("第三方发货员".equals(adminRole.getRoleName())){
                return null;
            }
            if( null != wechatUser){
                return wechatUser.getOpenId();
            }
        }
        return null;
    }

    /**
     * 管理员登陆
     * @param userName
     * @param code
     * @param ip
     * @return
     */
    public AdminUser loginByWechatCode(String userName,String code,String ip,HttpServletResponse response) throws UnsupportedEncodingException {
        AdminUser adminUser = adminUserService.findByUserName(userName);
        if( null ==adminUser){
            return null;
        }
        WechatUser wechatUser = wechatUserService.findById(adminUser.getWechatId());
        if( null == wechatUser){
            return null;
        }

        Object object = redisService.get(LoginCodeCacheKey.LOGIN_CODE_CACHE_KEY+wechatUser.getOpenId());
        if( null != object){
            String codeInCache = (String)object;
            if(!codeInCache.equals(code)){
                return null;
            }
        }else{

            return null;
        }


        adminUser.setLastLoginIp(ip);
        adminUser.setLastLoginTime(new Date());
        adminUserService.updateLoginInfoById(adminUser);

        String key = adminUserService.createAuthKey(adminUser);
        Cookie authKeyCookie = new Cookie(CommonConst.COOKIE_AUTH_KEY, key);
        authKeyCookie.setMaxAge(3600*3);
        authKeyCookie.setPath("/");

        Cookie nickCookie = new Cookie(CommonConst.COOKIE_NICK, URLEncoder.encode(adminUser.getNick(), "UTF-8") );
        nickCookie.setMaxAge(3600*24*30);
        nickCookie.setPath("/");

        response.addCookie(authKeyCookie);
        response.addCookie(nickCookie);

        if(!StringUtils.isEmpty(adminUser.getRoleId())) {
            Cookie roleCookie = new Cookie(CommonConst.COOKIE_ROLE, URLEncoder.encode(adminUser.getRoleId(), "UTF-8"));
            roleCookie.setMaxAge(3600 * 24 * 30);
            roleCookie.setPath("/");
            response.addCookie(roleCookie);
        }

        return adminUser;
    }


}
