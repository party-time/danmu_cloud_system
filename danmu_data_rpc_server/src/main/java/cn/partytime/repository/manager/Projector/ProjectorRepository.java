package cn.partytime.repository.manager.Projector;

import cn.partytime.model.projector.Projector;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by dm on 2017/6/14.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface ProjectorRepository extends MongoRepository<Projector, String> {

    Projector findByRegisterCode(String registerCode);

}
