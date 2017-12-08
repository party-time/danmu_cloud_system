package cn.partytime.service;

import cn.partytime.model.manager.MovieAlias;
import cn.partytime.model.manager.MovieSchedule;
import cn.partytime.model.manager.PartyAddressRelation;
import cn.partytime.repository.manager.MovieScheduleRepository;
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

/**
 * Created by administrator on 2016/12/1.
 */
@Service
public class MovieScheduleService {

    @Autowired
    private MovieScheduleRepository movieScheduleRepository;

    @Resource(name = "managerMongoTemplate")
    private MongoTemplate managerMongoTemplate;


    public MovieSchedule insertMovieSchedule(MovieSchedule movieSchedule){
        return movieScheduleRepository.insert(movieSchedule);
    }

    public MovieSchedule updateMovieSchedule(MovieSchedule movieSchedule){
        return movieScheduleRepository.save(movieSchedule);
    }

    public List<MovieSchedule> findByPartyIdAndAddressId(String partyId, String addressId){
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("partyId").is(partyId),
                Criteria.where("addressId").is(addressId));
        Query query = new Query(criteria);
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        query.with(sort);
        return managerMongoTemplate.find(query, MovieSchedule.class);
    }

    public Page<MovieSchedule> findAllByAddressId(String addressId,int pageSize,int pageNo){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(pageNo, pageSize, sort);
        return movieScheduleRepository.findByAddressId(addressId,pageRequest);
    }

    public Page<MovieSchedule> findAll(String partyId,int pageSize , int pageNo){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(pageNo, pageSize, sort);
        return movieScheduleRepository.findByPartyId(partyId,pageRequest);
    }

    public Page<MovieSchedule> findPageByPartyIdAndAddressIsAndCreateTimeDesc(String partyId,String addressId,int pageSize,int pageNo){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(pageNo, pageSize, sort);
        return movieScheduleRepository.findByPartyIdAndAddressId(partyId,addressId,pageRequest);
    }

    public Page<MovieSchedule> findPageByPartyIdAndAddressIs(String partyId,String addressId,int pageSize,int pageNo){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(pageNo, pageSize, sort);
        return movieScheduleRepository.findByPartyIdAndAddressId(partyId,addressId,pageRequest);
    }

    public List<MovieSchedule> findAll(){
       return movieScheduleRepository.findAll();
    }

    public void del(String id){
        movieScheduleRepository.delete(id);
    }

    public long countByCreateTimeGreaterThan(Date createTime){
        return movieScheduleRepository.countByCreateTimeGreaterThan(createTime);
    }
}
