package cn.partytime.model;

import cn.partytime.model.manager.AdminUser;
import cn.partytime.model.shop.Item;
import cn.partytime.model.shop.Order;
import cn.partytime.model.wechat.WechatUser;

/**
 * Created by administrator on 2017/7/7.
 */
public class OrderItem {

    private Order order;

    private Item item;

    private AdminUser adminUser;

    private WechatUser wechatUser;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public WechatUser getWechatUser() {
        return wechatUser;
    }

    public void setWechatUser(WechatUser wechatUser) {
        this.wechatUser = wechatUser;
    }
}
