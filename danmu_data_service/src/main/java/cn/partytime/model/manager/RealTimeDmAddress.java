package cn.partytime.model.manager;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by administrator on 2018/1/2.
 */
@Document(collection = "real_time_dm_address")
public class RealTimeDmAddress {

    private String id;

    private String name;

    private String parentId;

    private String addressId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}
