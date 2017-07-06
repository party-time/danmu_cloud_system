package cn.partytime.repository.manager;

import cn.partytime.model.manager.DanmuAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by liuwei on 16/6/15.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface DanmuAddressRepository extends MongoRepository<DanmuAddress,String> {

    public DanmuAddress findById(String Id);

    Page<DanmuAddress> findByIdNotIn(List<String> addressIds, Pageable pageable);

    List<DanmuAddress> findByIdIn(List<String> ids);

    Page<DanmuAddress> findByIdIn(List<String> ids, Pageable pageable);

    Page<DanmuAddress> findByType(Integer type, Pageable pageable);

    List<DanmuAddress> findByType(Integer type);



}
