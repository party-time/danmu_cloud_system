package cn.partytime.model.manager;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by administrator on 2017/2/7.
 */
public class AdminRole {

    @Field("_id")
    private String id;

    private String roleName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
