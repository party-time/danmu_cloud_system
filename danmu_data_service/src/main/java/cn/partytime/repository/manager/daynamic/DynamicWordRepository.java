package cn.partytime.repository.manager.daynamic;

import cn.partytime.model.daynamic.DynamicWordMode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface DynamicWordRepository extends MongoRepository<DynamicWordMode, String> {

}
