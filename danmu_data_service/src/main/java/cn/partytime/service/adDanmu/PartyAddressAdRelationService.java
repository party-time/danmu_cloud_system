package cn.partytime.service.adDanmu;

import cn.partytime.model.manager.PartyAddressAdRelation;
import cn.partytime.repository.manager.PartyAddressAdRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

@Service
public class PartyAddressAdRelationService {

    @Autowired
    private PartyAddressAdRelationRepository partyAddressAdRelationRepository;

    @Resource(name = "managerMongoTemplate")
    private MongoTemplate managerMongoTemplate;

    public PartyAddressAdRelation findPartyAddressAdRelationByPartyIdAndAddressId(String partyId, String addressId){
        return partyAddressAdRelationRepository.findByPartyIdAndAddressId(partyId,addressId);
    }
    public List<PartyAddressAdRelation> findPartyAddressAdRelationByPartyIdAndAddressIdList(String partyId, List<String> addList){
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("partyId").is(partyId),
                Criteria.where("addressId").in(addList));
        Query query = new Query(criteria);
        return managerMongoTemplate.find(query, PartyAddressAdRelation.class);
    }
    public List<PartyAddressAdRelation> findPartyAddressAdRelationByAddressIdAndPartyIdList(String addressId, List<String> partyIdList){
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("addressId").is(addressId),
                Criteria.where("partyId").in(partyIdList));
        Query query = new Query(criteria);
        return managerMongoTemplate.find(query, PartyAddressAdRelation.class);
    }


    public void insertPartyAddressAdRelation(PartyAddressAdRelation partyAddressAdRelation){
        partyAddressAdRelationRepository.insert(partyAddressAdRelation);
    }

    public void updatePartyAddressAdRelation(PartyAddressAdRelation partyAddressAdRelation){
        partyAddressAdRelationRepository.save(partyAddressAdRelation);
    }

    public void deleteByPartyIdAndAddressId(String partyId,String addressId){
        partyAddressAdRelationRepository.deleteByPartyIdAndAddressId(partyId,addressId);
    }

    public void deletePartyAddressAdRelation(String id){
        partyAddressAdRelationRepository.delete(id);
    }



}
