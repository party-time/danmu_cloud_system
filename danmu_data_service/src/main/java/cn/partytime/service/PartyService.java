package cn.partytime.service;

import cn.partytime.common.cachekey.FunctionControlCacheKey;
import cn.partytime.model.manager.Party;
import cn.partytime.model.manager.PartyAddressRelation;
import cn.partytime.redis.service.RedisService;
import cn.partytime.repository.manager.PartyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liuwei on 16/6/15.
 */

@Service
@Slf4j
public class PartyService {

    @Autowired
    private PartyRepository partyRepository;

    @Resource(name = "managerMongoTemplate")
    private MongoTemplate managerMongoTemplate;

    @Autowired
    private PartyAddressRelationService partyAddressRelationService;

    @Autowired
    private DanmuLibraryPartyService danmuLibraryPartyService;

    @Autowired
    private RedisService redisService;

    public Party save(String name, Integer type , String movieAlias, Date startTime, Date endTime,
                      String shortName,String danmuLibraryId,Integer dmDensity) {
        /**
        if (!StringUtils.isEmpty(shortName)) {
            Party party = findByShortName(shortName);
            if (null != party) {
                throw new IllegalArgumentException("拼音首字母有重复");
            }
        } else {
            throw new IllegalArgumentException("拼音首字母不能为空");
        }
         **/
        Party partyModel = new Party();
        partyModel.setName(name);
        partyModel.setStartTime(startTime);
        partyModel.setEndTime(endTime);
        partyModel.setShortName(shortName);
        partyModel.setType(type);
        partyModel.setMovieAlias(movieAlias);
        partyModel.setDmDensity(dmDensity);
        Party party = partyRepository.insert(partyModel);

        //String key = FunctionControlCacheKey.FUNCITON_CONTROL_DANMU_DENSITY + party.getId();
        //redisService.set(key,dmDensity);


        //danmuLibraryPartyService.save(danmuLibraryId,partyModel.getId());
        return party;
    }


    public Party update(String id ,String name, Integer type , String movieAlias, String danmuLibraryId,Integer dmDensity){
        Party party = this.findById(id);
        if( null != party){
            party.setName(name);
            party.setType(type);
            party.setMovieAlias(movieAlias);
            party.setDmDensity(dmDensity);
            //danmuLibraryPartyService.save(danmuLibraryId,party.getId());
            partyRepository.save(party);

            //String key = FunctionControlCacheKey.FUNCITON_CONTROL_DANMU_DENSITY + party.getId();
            //redisService.set(key,dmDensity);
        }

        return party;
    }

    public Party findById(String partyId) {
        return partyRepository.findOne(partyId);
    }

    public List<Party> findByIds(List<String> partyIds) {
        return partyRepository.findByIdIn(partyIds);
    }

    public void updateParty(Party party) {
        if (party == null) {
            throw new IllegalArgumentException("活动不存在");
        }
        partyRepository.save(party);
    }

    public Page<Party> findAllByPage(int page, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, pageSize, sort);
        return partyRepository.findAll(pageRequest);
    }

    public Page<Party> findPageByTypeAndStatus(int page, int pageSize , Integer type ,Integer status ) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, pageSize, sort);
        return partyRepository.findByTypeAndStatus(type,status,pageRequest);
    }

    public Page<Party> findPageByType(int page, int pageSize , Integer type ) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, pageSize, sort);
        return partyRepository.findByType(type,pageRequest);


    }

    public Page<Party> findAllPageByStatus(int page, int pageSize , Integer status ) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, pageSize, sort);
        if( status == 1){
            return partyRepository.findByStatusLessThan(3,pageRequest);
        }else if(status ==2){
            return partyRepository.findByStatusGreaterThan(2,pageRequest);
        }else{
            return partyRepository.findAll(pageRequest);
        }
    }


    public void updateById(String id, String name, Date startTime, Date endTime, Date lastUpdateResourceTime) {
        Party partyModel = partyRepository.findOne(id);
        if (null != partyModel) {
            if (!StringUtils.isEmpty(name)) {
                partyModel.setName(name);
            }
            if (null != startTime) {
                partyModel.setStartTime(startTime);
            }
            if (null != endTime) {
                partyModel.setEndTime(endTime);
            }
            if (null != lastUpdateResourceTime) {
                partyModel.setLastUpdateResourceTime(lastUpdateResourceTime);
            }
            partyRepository.save(partyModel);
        }

    }

    public void deleteById(String id) {
        partyRepository.delete(id);
        //删除活动后同时删除活动与场地的关联表
        List<PartyAddressRelation> partyAddressRelationList = partyAddressRelationService.findByPartyId(id);
        if (null != partyAddressRelationList && partyAddressRelationList.size() > 0) {
            for (PartyAddressRelation partyAddressRelation : partyAddressRelationList) {
                partyAddressRelationService.del(partyAddressRelation.getId());
            }
        }

    }

    public Party findByShortName(String shortName) {
        return partyRepository.findByShortName(shortName);
    }

    public List<Party> findByEndTime(Date date) {
        return partyRepository.findByEndTimeGreaterThan(date);
    }


    /**
     * @param delete 删除状态
     * @return
     */
    public List<Party> findPartyByDleteFlg(int delete) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("status").gt(0),
                Criteria.where("status").lt(3),
                Criteria.where("isDelete").is(delete)
        );
        Query query = new Query();
        query.addCriteria(criteria);
        return managerMongoTemplate.find(query, Party.class);
    }

    public List<Party> findByNameLike(String name) {
        return partyRepository.findByNameLike(name);
    }


    public List<Party> findByAddressIdAndStatus(String addressId , Integer status) {
        List<PartyAddressRelation> partyAddressRelationList = partyAddressRelationService.findByAddressId(addressId);
        if( null != partyAddressRelationList && partyAddressRelationList.size() > 0){
            List<String> ids = new ArrayList<>();
            for(PartyAddressRelation partyAddressRelation : partyAddressRelationList){
                ids.add(partyAddressRelation.getPartyId());
            }
            return partyRepository.findByIdInAndStatusLessThanAndType(ids,2,status);
        }else{
            return null;
        }
    }

    /**
     * 查找该场地下所有未结束的活动
     * @param addressId
     * @param type
     * @return
     */
    public List<Party> findByAddressIdAndType(String addressId , Integer type) {
        List<PartyAddressRelation> partyAddressRelationList = partyAddressRelationService.findByAddressId(addressId);
        if( null != partyAddressRelationList && partyAddressRelationList.size() > 0){
            List<String> ids = new ArrayList<>();
            for(PartyAddressRelation partyAddressRelation : partyAddressRelationList){
                ids.add(partyAddressRelation.getPartyId());
            }
            return partyRepository.findByIdInAndStatusLessThanAndType(ids,3,type);
        }else{
            return null;
        }
    }

    /**
     * 根据类型
     * @param type
     * @return
     */
    public List<Party> findByTypeAndStatus(Integer type,Integer status){
        return partyRepository.findByTypeAndStatusLessThan(type,status);
    }

    public List<Party> findByStartTimeBetween(Date fromDate, Date toDate) {
        return partyRepository.findByStartTimeBetweenAndStatus(fromDate, toDate,0);
    }


    public Party findByName(String name){
        return partyRepository.findByName(name);
    }

    /**
     * 查询未下线的电影
     * @param movieAlias
     * @return
     */
    public List<Party> findByMovieAlias(String movieAlias){
        return partyRepository.findByMovieAliasAndStatusNot(movieAlias,4);
    }

    /**
     * 电影下线
     * @param partyId
     */
    public void offlineParty(String partyId){
        Party partyModel = partyRepository.findOne(partyId);
        if( null != partyModel){
            partyModel.setStatus(4);
            partyRepository.save(partyModel);
        }
    }

    public List<Party> findOnlineMovie(){
        return partyRepository.findByTypeAndStatusNot(1,4);
    }



    /**
     * 查询在线上的电影
     * @param movieAlias
     * @return
     */
    public Party findByMovieAliasOnLine(String movieAlias){
        return partyRepository.findByMovieAliasAndStatus(movieAlias,0);
    }

    public List<Party> findAllPartyList(){
        return partyRepository.findAll();
    }

    public void setH5TempId(String partyId,String h5TempId){
        Party party = this.findById(partyId);
        if( null != party){
            party.setH5TempId(h5TempId);
        }
        partyRepository.save(party);
    }


}
