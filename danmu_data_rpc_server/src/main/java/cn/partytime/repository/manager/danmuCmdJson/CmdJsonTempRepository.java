package cn.partytime.repository.manager.danmuCmdJson;

import cn.partytime.model.manager.danmuCmdJson.CmdTemp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by administrator on 2017/5/8.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface CmdJsonTempRepository extends MongoRepository<CmdTemp,String> {

    public List<CmdTemp> findByIdIn(List<String> idList);

    public List<CmdTemp> findByKey(String key);


}
