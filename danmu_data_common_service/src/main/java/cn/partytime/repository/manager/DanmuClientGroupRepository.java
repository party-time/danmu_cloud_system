package cn.partytime.repository.manager;

import cn.partytime.model.client.DanmuClientGroup;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by liuwei on 16/6/15.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface DanmuClientGroupRepository extends MongoRepository<DanmuClientGroup,String> {

    public DanmuClientGroup findById(String id);
}
