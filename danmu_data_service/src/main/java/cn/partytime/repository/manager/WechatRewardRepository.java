package cn.partytime.repository.manager;

import cn.partytime.model.manager.WechatReward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by administrator on 2016/11/29.
 */
public interface WechatRewardRepository extends MongoRepository<WechatReward,String> {

    Page<WechatReward> findAll(Pageable pageable);

}
