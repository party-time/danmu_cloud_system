package cn.partytime.repository.manager;

import cn.partytime.model.manager.PartyAddressRelation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by liuwei on 2016/9/1.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface PartyAddressRelationRepository extends MongoRepository<PartyAddressRelation,String> {

    List<PartyAddressRelation> findByPartyId(String partyId);

    List<PartyAddressRelation> findByPartyIdAndAddressIdIn(String partyId,List<String> addressIdList);

    List<PartyAddressRelation> findByAddressId(String addressId);

    PartyAddressRelation findById(String id);

    //PartyAddressRelation findByPartyIdAndAddressId(String partyId,String addressId);

    List<PartyAddressRelation> findByIdIn(List<String> ids);

    List<PartyAddressRelation> findByPartyIdIn(List<String> partyIds);

    PartyAddressRelation findByPartyIdAndAddressId(String partyId,String addressId);

}
