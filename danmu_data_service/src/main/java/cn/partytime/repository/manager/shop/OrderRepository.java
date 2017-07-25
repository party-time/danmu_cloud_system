package cn.partytime.repository.manager.shop;

import cn.partytime.model.shop.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;

/**
 * Created by administrator on 2017/7/7.
 */
public interface OrderRepository extends MongoRepository<Order, String> {

    public Order findByCode(String code);

    public Order findByCodeAndGetItemTime(String code, Date getItemTime);

}
