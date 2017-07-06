package cn.partytime.service;

import cn.partytime.model.user.UserPrize;
import cn.partytime.repository.manager.UserPrizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lENOVO on 2016/10/18.
 */

@Service
public class UserPrizeService {

    @Resource(name = "managerMongoTemplate")
    private MongoTemplate managerMongoTemplate;

    @Autowired
    private UserPrizeRepository userPrizeRepository;

    /**
     * 保存用户获奖信息
     * @param userPrize
     */
    public UserPrize save(UserPrize userPrize) {
        return  userPrizeRepository.save(userPrize);
    }

    /**
     * 通过弹幕编号 和 openId 获取用户获奖信息
     * @param danmuId
     * @param openId
     * @return
     */
    public UserPrize findByDanmuIdAndOpenId(String danmuId, String openId) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("openId").is(openId),
                Criteria.where("danmuId").is(danmuId)
        );
        Query query = new Query();
        query.addCriteria(criteria);
        return managerMongoTemplate.findOne(query, UserPrize.class);
    }

    /**
     * 通过弹幕id list获取用户获奖信息
     * @param danmuIdList
     * @return
     */
    public List<UserPrize> findUserPrizeByDanmuList(List<String> danmuIdList) {
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("danmuId").in(danmuIdList)
        );
        Query query = new Query();
        query.addCriteria(criteria);
        return managerMongoTemplate.find(query, UserPrize.class);
    }

}
