package cn.partytime.repository.manager.danmuCmdJson;

import cn.partytime.model.manager.danmuCmdJson.CmdComponentValue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by administrator on 2017/5/8.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface CmdComponentValueRepository extends MongoRepository<CmdComponentValue,String> {

    List<CmdComponentValue> findByComponentId(String componentId);
}
