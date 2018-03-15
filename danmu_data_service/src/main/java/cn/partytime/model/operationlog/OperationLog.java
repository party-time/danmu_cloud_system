package cn.partytime.model.operationlog;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "operation_log")
public class OperationLog {

    private String id;

    private String adminUserId;

    private Date createTime;

    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
