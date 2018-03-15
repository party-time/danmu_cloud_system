package cn.partytime.repository.manager;

import cn.partytime.model.operationlog.OperationLogTemp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface OperationLogTempRepository extends MongoRepository<OperationLogTemp,String> {

    OperationLogTemp findByKey(String key);

    Integer countByKey(String key);

}
