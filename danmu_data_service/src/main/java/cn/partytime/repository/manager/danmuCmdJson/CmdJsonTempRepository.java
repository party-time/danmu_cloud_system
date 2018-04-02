package cn.partytime.repository.manager.danmuCmdJson;

import cn.partytime.model.manager.danmuCmdJson.CmdTemp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by administrator on 2017/5/8.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface CmdJsonTempRepository extends MongoRepository<CmdTemp,String> {

    List<CmdTemp> findByIdIn(List<String> idList);

    CmdTemp findByKey(String key);

    Integer countByKey(String key);

    Page<CmdTemp> findByType(Integer type,Pageable pageable);


}
