package cn.partytime.repository.manager;

import cn.partytime.model.manager.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by administrator on 2017/8/29.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface ReportRepository  extends MongoRepository<Report,String> {

    Report findByWechatIdAndDanmuId(String wechatId,String danmuId);

}
