package cn.partytime.repository.manager;

import cn.partytime.model.manager.AdminUserCompany;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by liuwei on 16/6/15.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface AdminUserCompanyRepository extends MongoRepository<AdminUserCompany, String> {

    public AdminUserCompany findById(String id);

    public AdminUserCompany findByAdminUserId(String adminUserId);

}
