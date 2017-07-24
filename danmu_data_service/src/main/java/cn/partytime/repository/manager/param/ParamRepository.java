package cn.partytime.repository.manager.param;

import cn.partytime.model.manager.Param;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by administrator on 2017/2/23.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface ParamRepository extends MongoRepository<Param,String> {

    List<Param> findByIdIn(List<String> paramIdList);

    List<Param> findByParamTemplateId(String paramTemplateId);
}
