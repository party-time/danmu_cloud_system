package cn.partytime.model;

import cn.partytime.model.manager.AdminUser;
import cn.partytime.model.operationlog.OperationLog;

public class OperationLogResult {

    private AdminUser adminUser;

    private OperationLog operationLog;

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public OperationLog getOperationLog() {
        return operationLog;
    }

    public void setOperationLog(OperationLog operationLog) {
        this.operationLog = operationLog;
    }
}
