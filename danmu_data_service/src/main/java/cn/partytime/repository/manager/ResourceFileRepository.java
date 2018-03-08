package cn.partytime.repository.manager;

import cn.partytime.model.manager.ResourceFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by liuwei on 2016/8/19.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface ResourceFileRepository extends MongoRepository<ResourceFile, String> {


    ResourceFile findById(String id);

    Page<ResourceFile> findByFileType(Integer fileType, Pageable pageable);

    Page<ResourceFile> findByFileTypeAndResourceNameLike(Integer fileType, String resourceName,Pageable pageable);

    List<ResourceFile> findByIdIn(List<String> idList );

    List<ResourceFile> findByFileType(Integer fileType);




}
