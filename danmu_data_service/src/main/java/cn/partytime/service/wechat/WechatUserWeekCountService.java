package cn.partytime.service.wechat;


import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.model.wechat.WechatUserWeekCount;
import cn.partytime.repository.user.WechatUserWeekCountRepository;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class WechatUserWeekCountService {

    @Resource(name = "userMongoTemplate")
    private MongoTemplate mongoTemplate;

    @Autowired
    private WechatUserWeekCountRepository wechatUserWeekCountRepository;

    public List<WechatUserWeekCount> findAll(Date startDate,Date endDate){
        return wechatUserWeekCountRepository.findByStartDateBetween(startDate,endDate);
    }

    public Page<WechatUserWeekCount> findAllByPage(Date startDate,Date endDate,Integer page, Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return wechatUserWeekCountRepository.findByStartDateAndEndDate(startDate,endDate,pageRequest);
    }

    public WechatUserWeekCount findByAddressIdAndStartDateAndEndDate(String addressId,Date startDate, Date endDate){
        //WechatUserWeekCount wechatUserWeekCount =  wechatUserWeekCountRepository.findByAddressIdAndStartDateAfterAndEndDateBefore(addressId,startDate,endDate);
        //log.info("addressId:{},startDate:{},endDate:{},wechatUserWeekCount:{}",addressId,startDate,endDate,JSON.toJSONString(wechatUserWeekCount));
        //return wechatUserWeekCount;

        //List<WechatUserWeekCount> wechatUserWeekCountList = wechatUserWeekCountRepository.findAll();
        //wechatUserWeekCountList = wechatUserWeekCountRepository.findByAddressIdAndStartDateGreaterThanEqual(addressId,startDate);


        //wechatUserWeekCountList = wechatUserWeekCountRepository.findByAddressId(addressId);

        /*List<WechatUserWeekCount> wechatUserWeekCountList =wechatUserWeekCountRepository.findByAddressIdAndStartDateGreaterThanEqualAndEndDateLessThanEqual(addressId,startDate,endDate);


        WechatUserWeekCount wechatUserWeekCount = null;
        if(ListUtils.checkListIsNotNull(wechatUserWeekCountList)){
            wechatUserWeekCount = wechatUserWeekCountList.get(0);
        }
        log.info("addressId:{},startDate:{},endDate:{},wechatUserWeekCount:{}",addressId,startDate,endDate,JSON.toJSONString(wechatUserWeekCount));
        return wechatUserWeekCount;*/

        /*Query query = new Query();
        Criteria criteria = Criteria.where("startDate").gte(startDate).lte(startDate).;
        query.addCriteria(criteria);
        return mongoTemplate.count(query, Table.class, "tabletest");*/

        /*List<WechatUserWeekCount> wechatUserWeekCountList = wechatUserWeekCountRepository.findAll();
        WechatUserWeekCount wechatUserWeekCountTemp = wechatUserWeekCountList.get(0);

        System.out.println("start=====================:"+DateUtils.dateToString(startDate,"yyyy-MM-dd HH:mm:ss"));
        System.out.println("start:"+DateUtils.dateToString(wechatUserWeekCountTemp.getStartDate(),"yyyy-MM-dd HH:mm:ss"));
        if(startDate.equals(wechatUserWeekCountTemp.getStartDate())){
            System.out.println("=============================start");
        }

        if(endDate.equals(wechatUserWeekCountTemp.getEndDate())){
            System.out.println("=============================end");
        }*/


        Criteria criteria = new Criteria().andOperator(
                Criteria.where("startDate").gte(startDate).lte(startDate),
                Criteria.where("endDate").gte(endDate).lte(endDate),
                Criteria.where("addressId").is(addressId));
        Query query = new Query(criteria);
        WechatUserWeekCount wechatUserWeekCount = mongoTemplate.findOne(query,WechatUserWeekCount.class);
        return wechatUserWeekCount;

    }

    public void save(WechatUserWeekCount wechatUserWeekCount){
        wechatUserWeekCountRepository.save(wechatUserWeekCount);
    }

}
