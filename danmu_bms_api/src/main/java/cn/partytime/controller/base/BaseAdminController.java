package cn.partytime.controller.base;

import cn.partytime.model.manager.AdminUser;

/**
 * Created by administrator on 2016/11/24.
 */
public class BaseAdminController {

    private AdminUser adminUser;

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }
}
