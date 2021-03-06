package cn.partytime.repository.manager.shop;

import cn.partytime.model.shop.Item;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by administrator on 2017/6/26.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface ItemRepository extends MongoRepository<Item, String> {

    List<Item> findByIdIn(List<String> idList);
}
