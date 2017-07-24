package cn.partytime.service;

import cn.partytime.model.manager.PartyAddressRelation;
import cn.partytime.repository.manager.PartyAddressRelationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by liuwei on 2016/9/1.
 */

@Service
@Slf4j
public class PartyAddressRelationService {

    @Resource(name = "managerMongoTemplate")
    private MongoTemplate managerMongoTemplate;

    @Autowired
    private PartyAddressRelationRepository partyAddressRelationRepository;

    @Autowired
    private DanmuPoolService danmuPoolService;

    @Autowired
    private PartyService partyService;

    @Autowired
    private DanmuAddressService danmuAddressService;

    public void save(String partyId, String addressId) {
        /*if (StringUtils.isEmpty(partyId) || StringUtils.isEmpty(addressId)) {
            throw new IllegalArgumentException("活动id和场地的id不能为空");
        }

        List<PartyAddressRelation> partyAddressRelationList = partyAddressRelationRepository.findByAddressId(addressId);
        if (null != partyAddressRelationList && partyAddressRelationList.size() > 0) {
            List<String> partyIds = new ArrayList<String>();
            partyIds.add(partyId);
            for (PartyAddressRelation partyAddressRelation : partyAddressRelationList) {
                if (!StringUtils.isEmpty(partyAddressRelation.getPartyId())) {
                    partyIds.add(partyAddressRelation.getPartyId());
                }
            }
            List<Party> partyList = partyService.findByIds(partyIds);
            if (null != partyList) {
                Party insertParty = null;
                for (Party party : partyList) {
                    if (party.getId().equals(partyId)) {
                        insertParty = party;
                    }
                }
                //判断关联场地时，同一个时间只能有一个活动
                for (Party party : partyList) {
                    if (!(insertParty.getStartTime().getTime() < party.getEndTime().getTime()) || !(insertParty.getEndTime().getTime() > party.getStartTime().getTime())) {
                        throw new IllegalArgumentException("同一个场地同一个时间只能有一个活动");
                    }
                }
            }
        }*/
        PartyAddressRelation par = partyAddressRelationRepository.findByPartyIdAndAddressId(partyId,addressId);
        if( null == par){
            PartyAddressRelation partyAddressRelation = new PartyAddressRelation();
            partyAddressRelation.setPartyId(partyId);
            partyAddressRelation.setAddressId(addressId);

            //创建活动与场地的关系
            partyAddressRelationRepository.insert(partyAddressRelation);
            //当创建一个活动与场地关联时，同时创建一个弹幕池
            danmuPoolService.save(partyId, addressId);
        }
    }

    /**
     * 通过活动编号查询所有的关系信息
     * @param partyId
     * @return
     */
    public List<PartyAddressRelation> findByPartyId(String partyId) {
        return partyAddressRelationRepository.findByPartyId(partyId);
    }


    public List<PartyAddressRelation> findByPartyIdAndAddressList(String partyId, List<String>addressList) {
        return partyAddressRelationRepository.findByPartyIdAndAddressIdIn(partyId,addressList);
    }


    /**
     * 通过地址编号查询所有的关系信息
     * @param addressId
     * @return
     */
    public List<PartyAddressRelation> findByAddressId(String addressId) {
        return partyAddressRelationRepository.findByAddressId(addressId);
    }



    public PartyAddressRelation findByPartyIdAndAddressId(String partyId, String addressId) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("partyId").is(partyId),
                Criteria.where("addressId").is(addressId));
        Query query = new Query(criteria);
        return managerMongoTemplate.findOne(query, PartyAddressRelation.class);
    }

    public PartyAddressRelation findById(String id) {
        return partyAddressRelationRepository.findById(id);
    }

    public void del(String id) {
        partyAddressRelationRepository.delete(id);
    }

    /**
     * 删除与活动的管理
     * @param partyId
     * @param addressId
     */
    public void delByPartyIdAndAddressId(String partyId, String addressId) {
        PartyAddressRelation partyAddressRelation = this.findByPartyIdAndAddressId(partyId, addressId);
        if (null != partyAddressRelation) {
            partyAddressRelationRepository.delete(partyAddressRelation.getId());
        } else {
            throw new IllegalArgumentException("del partyAddressRelation by partyId and addressId, partyAddressRelation is null.");
        }
    }

    public void updateById(String id, String partyId, String addressId) {

    }

    public PartyAddressRelation findByAddressIdANDPartyIdWithin(String addressId, List<String> partyIdList) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("partyId").in(partyIdList),
                Criteria.where("addressId").is(addressId)
        );
        Query query = new Query(criteria);
        return managerMongoTemplate.findOne(query, PartyAddressRelation.class);
    }

    public List<PartyAddressRelation> findByPartyIdandaddressIdWithin(String partyId, List<String> addressIdList) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("addressId").in(addressIdList),
                Criteria.where("partyId").is(partyId)
        );
        Query query = new Query(criteria);
        return managerMongoTemplate.find(query, PartyAddressRelation.class);
    }


    public List<PartyAddressRelation> findByIds(List<String> ids) {
        return partyAddressRelationRepository.findByIdIn(ids);
    }

    public List<PartyAddressRelation> findByPartyIds(List<String> partyIds) {
        return partyAddressRelationRepository.findByPartyIdIn(partyIds);
    }


}
