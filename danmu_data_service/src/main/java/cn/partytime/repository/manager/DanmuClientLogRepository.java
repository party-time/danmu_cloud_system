package cn.partytime.repository.manager;

import cn.partytime.model.client.DanmuClientLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by liuwei on 16/6/15.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface DanmuClientLogRepository extends MongoRepository<DanmuClientLog,String> {

    public DanmuClientLog findById(String id);
}
