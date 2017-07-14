package cn.partytime.check.rpcService.impl;

import cn.partytime.check.rpcService.AdminUserService;
import cn.partytime.check.model.AdminUser;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/7.
 */

@Component
public class AdminUserServiceHystrix implements AdminUserService {
    @Override
    public AdminUser getAdminUser(String authKey) {
        return null;
    }

    @Override
    public Boolean checkAuthKey(String authKey) {
        return false;
    }
}
