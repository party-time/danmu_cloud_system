package cn.partytime.repository.user;

import cn.partytime.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by liuwei on 16/6/15.
 */

@EnableMongoRepositories(mongoTemplateRef = "userMongoTemplate")
public interface UserRepository extends MongoRepository<User,String> {

    public User findById(String id);
}
