package cn.partytime.rpc;

import cn.partytime.common.cachekey.AdminUserCacheKey;
import cn.partytime.model.manager.AdminUser;
import cn.partytime.redis.service.RedisService;
import cn.partytime.repository.manager.AdminUserRepository;
import cn.partytime.service.AdminUserService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dm on 2017/7/10.
 */

@RestController
@RequestMapping("/rpcAdmin")
public class RpcAdminService {

    @Autowired
    private AdminUserService  adminUserService;

    @Autowired
    private RedisService redisService;

    /**
     * 检查authkey是否存在
     * @param authKey
     * @return
     */

    @RequestMapping(value = "/checkAuthKey" ,method = RequestMethod.GET)
    public Boolean checkAuthKey(@RequestParam String authKey){
        String key = AdminUserCacheKey.ADMIN_USER_CACHE_KEY+authKey;
        return redisService.isEXIST(key);
    }


    /**
     * 根据authKey获得管理员
     * @param authKey
     * @return
     */
    @RequestMapping(value = "/getAdminUser" ,method = RequestMethod.GET)
    public AdminUser getAdminUser(@RequestParam String authKey){
        String key = AdminUserCacheKey.ADMIN_USER_CACHE_KEY+authKey;
        Object obj = redisService.get(key);
        if( null != obj){
            return JSON.parseObject((String)obj,AdminUser.class);
        }else{
            return null;
        }
    }

    @RequestMapping(value = "/findById" ,method = RequestMethod.GET)
    public AdminUser findById(@RequestParam String id){
        return adminUserService.findById(id);
    }
}
