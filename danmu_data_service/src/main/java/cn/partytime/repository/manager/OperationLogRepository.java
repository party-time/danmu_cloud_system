package cn.partytime.repository.manager;

import cn.partytime.model.operationlog.OperationLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface OperationLogRepository extends MongoRepository<OperationLog,String> {


}
