package cn.partytime.repository.user;

import cn.partytime.model.wechat.WechatUserLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by administrator on 2016/11/30.
 */
@EnableMongoRepositories(mongoTemplateRef = "userMongoTemplate")
public interface WechatUserLogRepository extends MongoRepository<WechatUserLog,String> {


}
