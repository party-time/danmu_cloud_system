package cn.partytime.repository.danmu;

import cn.partytime.model.danmu.DanmuLibrary;
import cn.partytime.model.danmu.DanmuLibraryParty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by liuwei on 2016/10/21.
 */
@EnableMongoRepositories(mongoTemplateRef = "danmuMongoTemplate")
public interface DanmuLibraryPartyRepository  extends MongoRepository<DanmuLibraryParty,String> {

    DanmuLibraryParty findByPartyIdAndDanmuLibraryId(String partyId,String libraryId);

    List<DanmuLibraryParty> findByPartyIdOrderByCreateTimeAsc(String partyId);

    List<DanmuLibraryParty> findByDanmuLibraryId(String danmuLibraryId);

    void deleteByPartyId(String partyId);
}
