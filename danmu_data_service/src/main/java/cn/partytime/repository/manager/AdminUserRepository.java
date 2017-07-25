package cn.partytime.repository.manager;

import cn.partytime.model.manager.AdminUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by liuwei on 16/6/15.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface AdminUserRepository extends MongoRepository<AdminUser,String> {

    AdminUser findByUserName(String userName);

    AdminUser findByNick(String nick);

    AdminUser findByUserNameAndPassword(String userName, String password);

    Page<AdminUser> findAll(Pageable pageable);

    AdminUser findByWechatId(String wechatId);

}
