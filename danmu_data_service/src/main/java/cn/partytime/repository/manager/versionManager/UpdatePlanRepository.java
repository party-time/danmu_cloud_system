package cn.partytime.repository.manager.versionManager;

import cn.partytime.model.manager.UpdatePlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by administrator on 2017/2/13.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface UpdatePlanRepository extends MongoRepository<UpdatePlan,String> {

    Page<UpdatePlan> findByAddressId(String addressId,Pageable pageable);

    List<UpdatePlan> findByAddressId(String addressId);

}
