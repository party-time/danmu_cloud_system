package cn.partytime.repository.manager.param;

import cn.partytime.model.manager.ParamTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by administrator on 2017/2/23.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface ParamTemplateRepository extends MongoRepository<ParamTemplate,String> {

    public List<ParamTemplate> findByIdIn(List<String> idList);

}
