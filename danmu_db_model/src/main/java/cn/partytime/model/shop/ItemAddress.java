package cn.partytime.model.shop;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by administrator on 2017/6/26.
 *
 * 商品和场地的关系
 */
public class ItemAddress {

    @Field("_id")
    private String id;

    private String itemId;

    private String addressId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}
