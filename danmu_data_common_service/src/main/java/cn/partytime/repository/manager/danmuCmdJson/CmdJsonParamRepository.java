package cn.partytime.repository.manager.danmuCmdJson;

import cn.partytime.model.manager.danmuCmdJson.CmdJsonParam;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by administrator on 2017/5/8.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface CmdJsonParamRepository extends MongoRepository<CmdJsonParam,String> {

    public List<CmdJsonParam> findByIdIn(List<String> paramIds);

    public List<CmdJsonParam> findByCmdJsonTempIdOrderBySortAsc(String cmdJsonTempId);


}
