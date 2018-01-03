package cn.partytime.repository.manager;

import cn.partytime.model.manager.RealTimeDmAddress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by administrator on 2018/1/3.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface RealTimeDmAddressRepository extends MongoRepository<RealTimeDmAddress,String> {

    List<RealTimeDmAddress> findByParentId(String parentId);

    RealTimeDmAddress findByAddressId(String addressId);


}
