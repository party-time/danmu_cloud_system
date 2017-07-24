package cn.partytime.repository.manager.shop;

import cn.partytime.model.shop.ItemAddress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by administrator on 2017/6/26.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface ItemAddressRepository extends MongoRepository<ItemAddress, String> {

}
