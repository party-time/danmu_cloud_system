package cn.partytime.repository.user;


import cn.partytime.model.wechat.WechatUserWeekCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Date;
import java.util.List;

@EnableMongoRepositories(mongoTemplateRef = "userMongoTemplate")
public interface WechatUserWeekCountRepository extends MongoRepository<WechatUserWeekCount,String> {

    public Page<WechatUserWeekCount> findAll(Pageable pageable);

    WechatUserWeekCount  findByAddressIdAndStartDateAfterAndEndDateBefore(String addressId,Date startDate, Date endDate);


    List<WechatUserWeekCount>  findByAddressIdAndStartDateGreaterThanEqual(String addressId, Date startDate);

    List<WechatUserWeekCount>  findByAddressId(String addressId);

    List<WechatUserWeekCount> findByStartDateAndEndDate(Date startDate, Date endDate);

    List<WechatUserWeekCount>  findByAddressIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(String addressId, Date startDate,Date endDate);



}
