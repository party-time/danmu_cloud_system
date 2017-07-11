package cn.partytime.service;

import cn.partytime.common.cachekey.AdminUserCacheKey;
import cn.partytime.common.constants.CommonConst;
import cn.partytime.model.manager.AdminUser;
import cn.partytime.redis.service.RedisService;
import cn.partytime.repository.manager.AdminUserRepository;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


/**
 * Created by liuwei on 16/6/15.
 */

@Service
@Slf4j
public class AdminUserService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private RedisService redisService;

    /**
     * 创建一个管理员账号
     * @param userName
     * @param password
     * @param nick
     * @return
     */
    public AdminUser createAdminUser(String userName, String password, String nick, String roleId, String wechatId ){
        if( isExistUserName(userName)){
            throw new IllegalArgumentException("用户名不能重复");
        }
        if(isExistNick(nick)){
            throw new IllegalArgumentException("姓名不能重复");
        }
        AdminUser adminUser = new AdminUser();
        userName = userName.trim();
        adminUser.setUserName(userName.toLowerCase());
        //密码进行加密处理
        password = BCrypt.hashpw(password,BCrypt.gensalt());
        adminUser.setPassword(password);
        adminUser.setNick(nick);
        adminUser.setRoleId(roleId);
        adminUser.setWechatId(wechatId);
        return save(adminUser);
    }

    public Boolean checkPassword(String id,String password){
        AdminUser adminUser = adminUserRepository.findOne(id);
        if( null == adminUser){
            return false;
        }
        return BCrypt.checkpw(password,adminUser.getPassword());

    }



    public String createAuthKey(AdminUser adminUser){
        String hashKey = BCrypt.hashpw(adminUser.getId(),BCrypt.gensalt());
        String key = AdminUserCacheKey.ADMIN_USER_CACHE_KEY+hashKey;
        redisService.set(key,JSON.toJSONString(adminUser),3600*3);
        return hashKey;
    }

    /**
     * 检查authkey是否存在
     * @param authKey
     * @return
     */
    public Boolean checkAuthKey(String authKey){
        String key = AdminUserCacheKey.ADMIN_USER_CACHE_KEY+authKey;
        return redisService.isEXIST(key);
    }


    /**
     * 根据authKey获得管理员
     * @param authKey
     * @return
     */
    public AdminUser getAdminUser(String authKey){
        String key = AdminUserCacheKey.ADMIN_USER_CACHE_KEY+authKey;
        Object obj = redisService.get(key);
        if( null != obj){
            return JSON.parseObject((String)obj,AdminUser.class);
        }else{
            return null;
        }
    }


    public AdminUser save(AdminUser adminUser){

        return  adminUserRepository.insert(adminUser);
    }

    public void deleteByUserName(String userName){
        AdminUser adminUser = this.findByUserName(userName);
        adminUserRepository.delete(adminUser);
    }

    public void deleteById(String id){
        adminUserRepository.delete(id);
    }

    public AdminUser updateAdminInfoById(AdminUser adminUser){
        if( null == adminUser || StringUtils.isEmpty(adminUser.getId())){
            throw new IllegalArgumentException("对象或者主键不能为空");
        }
        AdminUser au = adminUserRepository.findOne(adminUser.getId());
        if( null != au){
            if(!StringUtils.isEmpty(adminUser.getNick())){
                au.setNick(adminUser.getNick());
            }
            if(!StringUtils.isEmpty(adminUser.getUserName())){
                au.setUserName(adminUser.getUserName());
            }
            if(!StringUtils.isEmpty(adminUser.getPassword())){
                String password = BCrypt.hashpw(adminUser.getPassword(),BCrypt.gensalt());
                au.setPassword(password);
            }
            if(!StringUtils.isEmpty(adminUser.getRoleId())){
                au.setRoleId(adminUser.getRoleId());
            }

            au.setWechatId(adminUser.getWechatId());

        }
        return adminUserRepository.save(au);
    }

    public AdminUser updateLoginInfoById(AdminUser adminUser){
        return adminUserRepository.save(adminUser);
    }


    public AdminUser findByUserName(String userName){
        return adminUserRepository.findByUserName(userName);
    }


    public Page<AdminUser> findAll(int page, int size){
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        PageRequest pageRequest = new PageRequest(page,size,sort);
        return adminUserRepository.findAll(pageRequest);
    }

    /*public void addSessionTime(String cookieValue,HttpServletResponse response){
        String key = AdminUserCacheKey.ADMIN_USER_CACHE_KEY+cookieValue;
        redisService.expire(key,3600*3);

        Cookie authKeyCookie = new Cookie(CommonConst.COOKIE_AUTH_KEY, cookieValue);
        authKeyCookie.setMaxAge(3600*3);
        authKeyCookie.setPath("/");
        response.addCookie(authKeyCookie);

    }*/

    public Boolean isExistUserName(String userName){
        userName = userName.trim();
        AdminUser adminUser = adminUserRepository.findByUserName(userName.toLowerCase());
        if( null ==adminUser){
            return false;
        }else{
            return true;
        }
    }

    public Boolean isExistNick(String nick){
        nick = nick.trim();
        AdminUser adminUser = adminUserRepository.findByNick(nick);
        if( null ==adminUser){
            return false;
        }else{
            return true;
        }
    }

    public AdminUser findById(String id){
        return adminUserRepository.findOne(id);
    }


}
