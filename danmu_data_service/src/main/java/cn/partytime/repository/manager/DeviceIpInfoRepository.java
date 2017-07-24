package cn.partytime.repository.manager;

import cn.partytime.model.manager.DeviceIpInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by administrator on 2017/3/21.
 */
@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface DeviceIpInfoRepository extends MongoRepository<DeviceIpInfo,String> {

    List<DeviceIpInfo> findByAddressId(String addressId);

    List<DeviceIpInfo> findByAddressIdAndType(String addressId, Integer type);

}
