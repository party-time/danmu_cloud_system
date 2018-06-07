package cn.partytime.repository.danmu;

import cn.partytime.model.danmu.PayDanmu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by admin on 2018/6/7.
 */

@EnableMongoRepositories(mongoTemplateRef = "danmuMongoTemplate")
public interface PayDanmuRepository extends MongoRepository<PayDanmu, String> {


    PayDanmu findByDanmuId(String danmuId);
}
