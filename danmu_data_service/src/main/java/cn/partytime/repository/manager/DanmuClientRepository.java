package cn.partytime.repository.manager;

import cn.partytime.model.client.DanmuClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by liuwei on 16/6/15.
 */

@EnableMongoRepositories(mongoTemplateRef = "managerMongoTemplate")
public interface DanmuClientRepository extends MongoRepository<DanmuClient,String> {

    public DanmuClient findById(String id);

    public DanmuClient findByRegistCode(String registCode);

    public DanmuClient findByDanmuClientCode(String danmuClientCode);

    /**
     * 通过地址编号获取客户端
     * @param addressId
     * @return
     */
    public Page<DanmuClient> findByAddressId(Pageable pageRequest, String addressId);

    List<DanmuClient> findByAddressId(String addressId);




}
