package cn.partytime.repository.user;

import cn.partytime.model.wechat.WechatUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by liuwei on 16/7/21.
 */

@EnableMongoRepositories(mongoTemplateRef = "userMongoTemplate")
public interface WechatUserRepository extends MongoRepository<WechatUser, String> {

    public WechatUser findByUnionId(String unionId);

    public WechatUser findByOpenId(String openId);

    public Page<WechatUser> findAll(Pageable pageable);

    public Page<WechatUser> findByNickLike(String nick,Pageable pageable);

    public long countBySubscribeTimeBetween(long from, long to);

    public Page<WechatUser> findBySubscribeTimeBetween(long from, long to,Pageable pageable);

    public WechatUser findByUserId(String userId);

    public List<WechatUser> findByIdIn(List<String> idList);

    public Page<WechatUser> findByNickLikeAndSubscribeState(String nick,Integer subscribeState,Pageable pageable);

}
