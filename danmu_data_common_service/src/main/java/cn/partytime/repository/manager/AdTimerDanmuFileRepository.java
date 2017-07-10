package cn.partytime.repository.manager;

import cn.partytime.model.manager.AdTimerDanmuFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface AdTimerDanmuFileRepository  extends MongoRepository<AdTimerDanmuFile,String> {

    AdTimerDanmuFile findByPoolId(String poolId);

    List<AdTimerDanmuFile> findByPoolIdIn(List<String> poolId);

}
