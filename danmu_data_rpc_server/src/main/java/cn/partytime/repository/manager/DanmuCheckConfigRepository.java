package cn.partytime.repository.manager;

import cn.partytime.model.manager.DanmuCheckConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by user on 16/7/19.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface DanmuCheckConfigRepository extends MongoRepository<DanmuCheckConfig, String> {

    public DanmuCheckConfig findById(String id);

}
