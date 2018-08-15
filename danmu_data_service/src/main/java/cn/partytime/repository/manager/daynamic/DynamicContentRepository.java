package cn.partytime.repository.manager.daynamic;

import cn.partytime.model.daynamic.DynamicContentMode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface DynamicContentRepository extends MongoRepository<DynamicContentMode, String> {
}
