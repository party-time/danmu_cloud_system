package cn.partytime.repository.manager;

import cn.partytime.model.manager.MovieSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 2016/12/1.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface MovieScheduleRepository extends MongoRepository<MovieSchedule,String> {

    List<MovieSchedule> findByPartyIdAndAddressId(String partyId,String addressId);

    Page<MovieSchedule> findByPartyId(String partyId, Pageable pageable);

    Page<MovieSchedule> findByAddressId(String addressId, Pageable pageable);

    Page<MovieSchedule> findByPartyIdAndAddressId(String partyId,String addressId, Pageable pageable);

    long countByCreateTimeGreaterThanAndAddressId(Date createTime,String addressId);

}
