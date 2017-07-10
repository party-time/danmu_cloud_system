package cn.partytime.repository.manager.Projector;

import cn.partytime.model.projector.ProjectorAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Date;
import java.util.List;

/**
 * Created by dm on 2017/6/14.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface ProjectorActionRepository extends MongoRepository<ProjectorAction, String> {


    Page<ProjectorAction> findByRegisterCode(String registCode, Pageable pageable);



    List<ProjectorAction> findByRegisterCodeAndStartTimeAfterOrderByUpdateTimeDesc(String registCode, Date startDate);
}
