package cn.partytime.repository.manager.cms;

import cn.partytime.model.cms.Column;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by administrator on 2017/6/29.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface ColumnRepository extends MongoRepository<Column,String> {

    public List<Column> findByIdIn(List<String> idList);

    public List<Column> findByAddressId(String addressId);
}
