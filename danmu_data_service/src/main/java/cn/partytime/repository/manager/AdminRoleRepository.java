package cn.partytime.repository.manager;

import cn.partytime.model.manager.AdminRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by administrator on 2017/2/7.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface AdminRoleRepository extends MongoRepository<AdminRole,String> {


}
