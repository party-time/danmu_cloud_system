package cn.partytime.repository.user;


import cn.partytime.model.wechat.WechatUserInfo;
import cn.partytime.model.wechat.WechatUserWeekCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Date;
import java.util.List;

@EnableMongoRepositories(mongoTemplateRef = "userMongoTemplate")
public interface WechatUserWeekCountRepository extends MongoRepository<WechatUserWeekCount,String> {

    public Page<WechatUserWeekCount> findByStartDateAndEndDate(Date startDate,Date endDate,Pageable pageable);

    List<WechatUserWeekCount>  findByAddressId(String addressId);

    List<WechatUserWeekCount> findByStartDateBetween(Date startDate, Date endDate);



}
