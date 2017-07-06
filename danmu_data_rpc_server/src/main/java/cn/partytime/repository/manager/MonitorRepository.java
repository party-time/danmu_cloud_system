package cn.partytime.repository.manager;

import cn.partytime.model.monitor.Monitor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by administrator on 2017/6/20.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface MonitorRepository extends MongoRepository<Monitor,String> {

    Integer countByKey(String key);




}
