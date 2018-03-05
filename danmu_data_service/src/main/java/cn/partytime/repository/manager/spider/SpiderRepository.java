package cn.partytime.repository.manager.spider;

import cn.partytime.model.spider.Spider;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 2017/12/25.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface SpiderRepository extends MongoRepository<Spider,String> {

    public Spider findByOldId(String oldId);

    List<Spider> findByDateGreaterThanAndPartyIdIsNull(Date date);

    Spider findByPartyId(String partyId);
}
