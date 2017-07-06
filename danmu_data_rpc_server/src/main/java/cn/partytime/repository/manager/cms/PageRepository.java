package cn.partytime.repository.manager.cms;

import cn.partytime.model.cms.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by administrator on 2017/6/29.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface PageRepository extends MongoRepository<Page,String> {
    Page findByUrl(String url);
}
