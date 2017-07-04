package cn.partytime.repository.manager;

import cn.partytime.model.manager.PartyResource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by administrator on 2016/11/21.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface PartyResourceRepository extends MongoRepository<PartyResource, String> {

    List<PartyResource> findByPartyId(String partyId);

    List<PartyResource> findByResourceId(String resourceId);

    List<PartyResource> findByPartyIdAndFileType(String partyId, Integer fileType);

    PartyResource findByPartyIdAndResourceId(String partyId, String resourceId);


}
