package cn.partytime.repository.manager.danmuCmdJson;

import cn.partytime.model.manager.danmuCmdJson.CmdComponent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by administrator on 2017/5/8.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface CmdJsonComponentRepository extends MongoRepository<CmdComponent,String> {

}
