package cn.partytime.repository.user;


import cn.partytime.model.wechat.WechatUserWeekCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Date;

@EnableMongoRepositories(mongoTemplateRef = "userMongoTemplate")
public interface WechatUserWeekCountRepository extends MongoRepository<WechatUserWeekCount,String> {

    public Page<WechatUserWeekCount> findAll(Pageable pageable);

    WechatUserWeekCount  findByAddressIdAndStartDateAndEndDate(String addressId,Date startDate, Date endDate);


}
