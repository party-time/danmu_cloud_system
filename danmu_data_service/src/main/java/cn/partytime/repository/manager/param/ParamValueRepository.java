package cn.partytime.repository.manager.param;

import cn.partytime.model.manager.ParamValue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by administrator on 2017/2/23.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface ParamValueRepository extends MongoRepository<ParamValue,String> {

    public List<ParamValue> findByObjId(String objId);

    public List<ParamValue> findByObjIdAndTypeAndParamId(String objId, Integer type, String paramId);

    public List<ParamValue> findByObjIdAndTypeAndParamIdIn(String objId, Integer type, List<String> paramIdList);

    public List<ParamValue> findByObjIdAndType(String objId, Integer type);
}
