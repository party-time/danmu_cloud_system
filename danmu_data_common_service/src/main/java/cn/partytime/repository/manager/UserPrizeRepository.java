package cn.partytime.repository.manager;

import cn.partytime.model.user.UserPrize;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by lENOVO on 2016/10/18.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface UserPrizeRepository extends MongoRepository<UserPrize, String> {

}
