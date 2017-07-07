package cn.partytime.check.dataService.impl;

import cn.partytime.check.dataService.AdminUserService;
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
}
