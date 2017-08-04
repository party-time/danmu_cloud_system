package cn.partytime.repository.manager;

import cn.partytime.model.manager.LovePay;
import cn.partytime.model.monitor.Monitor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by administrator on 2017/7/25.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface LovePayRepository extends MongoRepository<LovePay,String> {

}
