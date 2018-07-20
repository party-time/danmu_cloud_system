package cn.partytime.repository.user;

import cn.partytime.model.wechat.WeChatMiniUser;
import cn.partytime.model.wechat.WechatUserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by Administrator on 2018/7/20 0020.
 */
@EnableMongoRepositories(mongoTemplateRef = "userMongoTemplate")
public interface WeChatMiniUserRepository extends MongoRepository<WeChatMiniUser,String> {

    public WeChatMiniUser findByUnionId(String unionId);

}
