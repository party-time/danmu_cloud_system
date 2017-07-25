package cn.partytime.repository.danmu;

import cn.partytime.model.danmu.DanmuLog;
import cn.partytime.model.danmu.DanmuModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by dm on 2017/5/16.
 */

@EnableMongoRepositories(mongoTemplateRef = "danmuMongoTemplate")
public interface DanmuLogRepository  extends MongoRepository<DanmuLog, String> {



    public Page<DanmuLog> findByIsBlocked(boolean isBlocked, Pageable pageable);


}
