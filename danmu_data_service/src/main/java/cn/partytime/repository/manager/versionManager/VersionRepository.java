package cn.partytime.repository.manager.versionManager;

import cn.partytime.model.manager.Version;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by administrator on 2017/2/13.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface VersionRepository extends MongoRepository<Version,String> {

    Page<Version> findAll(Pageable pageable);

    List<Version> findByVersionAndType(String version,Integer type);

    List<Version> findByIdIn(List<String> idList);

    Page<Version> findByIdNotIn(List<String> idList,Pageable pageable);

    List<Version> findByType(Integer type);
}
