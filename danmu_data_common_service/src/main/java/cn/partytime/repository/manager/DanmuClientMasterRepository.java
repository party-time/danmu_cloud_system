package cn.partytime.repository.manager;

import cn.partytime.model.client.DanmuClientMaster;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by liuwei on 16/6/15.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface DanmuClientMasterRepository extends MongoRepository<DanmuClientMaster,String> {

    public DanmuClientMaster findById(String id);
}
