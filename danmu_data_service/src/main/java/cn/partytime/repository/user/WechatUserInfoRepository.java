package cn.partytime.repository.user;

import cn.partytime.model.wechat.WechatUserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

/**
 * Created by administrator on 2017/4/13.
 */
@EnableMongoRepositories(mongoTemplateRef = "userMongoTemplate")
public interface WechatUserInfoRepository extends MongoRepository<WechatUserInfo,String> {

    WechatUserInfo findByWechatId(String wechatId);

    List<WechatUserInfo> findByWechatIdIn(List<String> wechatIdList);
}
