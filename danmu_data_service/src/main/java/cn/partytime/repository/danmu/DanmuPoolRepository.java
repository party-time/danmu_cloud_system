package cn.partytime.repository.danmu;

import cn.partytime.model.danmu.DanmuPool;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by liuwei on 16/6/15.
 */
@EnableMongoRepositories(mongoTemplateRef = "danmuMongoTemplate")
public interface DanmuPoolRepository extends MongoRepository<DanmuPool, String> {

    DanmuPool findById(String id);

    public DanmuPool findByIdAndIsDelete(String id, Integer isDelete);


    public List<DanmuPool> findByIdInAndIsDelete(List<String> ids, Integer isDelete);

    List<DanmuPool> findByIdIn(List<String> ids);

    public List<DanmuPool> findByPartyAddressRelationIdInAndIsDelete(List<String> partyAddressRelationIdList, Integer isDelete);







}
