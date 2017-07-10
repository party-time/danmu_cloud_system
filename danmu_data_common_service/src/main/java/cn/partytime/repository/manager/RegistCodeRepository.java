package cn.partytime.repository.manager;

import cn.partytime.model.manager.RegistCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by liuwei on 2016/9/7.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface RegistCodeRepository extends MongoRepository<RegistCode,String> {

    RegistCode findByRegistCode(String registCode);
    
}
