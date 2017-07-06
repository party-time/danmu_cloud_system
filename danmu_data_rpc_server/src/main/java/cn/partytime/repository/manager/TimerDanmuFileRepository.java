package cn.partytime.repository.manager;

import cn.partytime.model.manager.TimerDanmuFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by lENOVO on 2016/11/28.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface TimerDanmuFileRepository extends MongoRepository<TimerDanmuFile, String> {


    List<TimerDanmuFile> findByPartyId(String partyId);

}
