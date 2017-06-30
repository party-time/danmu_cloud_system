package cn.partytime.repository.manager;

import cn.partytime.model.manager.PartyAddressAdRelation;
import cn.partytime.model.manager.PartyAddressRelation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface PartyAddressAdRelationRepository extends MongoRepository<PartyAddressAdRelation,String> {

    PartyAddressAdRelation findByPartyIdAndAddressId(String partyId,String addressId);


    long deleteByPartyIdAndAddressId(String partyId,String addressId);

}
