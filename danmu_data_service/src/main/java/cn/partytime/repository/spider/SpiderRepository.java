package cn.partytime.repository.spider;

import cn.partytime.model.spider.Spider;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by administrator on 2017/12/25.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface SpiderRepository extends MongoRepository<Spider,String> {

    public Spider findByOldId(String oldId);

}
