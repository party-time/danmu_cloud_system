package cn.partytime.service.wechat;

import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.model.user.User;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.repository.user.WechatUserRepository;
import cn.partytime.service.UserService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by liuwei on 2016/9/28.
 */

@Service
@Slf4j
public class WechatUserService {

    @Autowired
    private WechatUserRepository wechatUserRepository;

    @Resource(name = "userMongoTemplate")
    private MongoTemplate userMongoTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private WechatUserLogService wechatUserLogService;

    public WechatUser findById(String id){
        return wechatUserRepository.findOne(id);
    }

    public WechatUser save(WechatUser wechatUser) {
        WechatUser oldWebchatUser = wechatUserRepository.findByOpenId(wechatUser.getOpenId());
        if (null != oldWebchatUser) {
            oldWebchatUser.setCity(wechatUser.getCity());
            oldWebchatUser.setProvince(wechatUser.getProvince());
            oldWebchatUser.setSex(wechatUser.getSex());
            oldWebchatUser.setCountry(wechatUser.getCountry());
            oldWebchatUser.setImgUrl(wechatUser.getImgUrl());
            oldWebchatUser.setLanguage(wechatUser.getLanguage());
            oldWebchatUser.setNick(wechatUser.getNick());
            oldWebchatUser.setLastOpenDate(new Date());
            oldWebchatUser.setUnionId(wechatUser.getUnionId());
            if (null != wechatUser.getSubscribeState()) {
                oldWebchatUser.setSubscribeState(wechatUser.getSubscribeState());
            }
            return wechatUserRepository.save(oldWebchatUser);
        } else {
            //保存用户信息
            Date date = DateUtils.getCurrentDate();
            User user = new User();
            user.setLastLoginTime(date);
            user.setCreateTime(date);
            user.setUpdateTime(date);
            user = userService.save(user);
            wechatUser.setUserId(user.getId());
            wechatUser.setLastOpenDate(new Date());
            wechatUser.setSubscribeState(0);
            wechatUser.setCreateDate(DateUtils.getCurrentDate());
            //保存微信用户信息
            return wechatUserRepository.insert(wechatUser);
        }
    }

    public WechatUser updateUserInfo(WechatUser wechatUser) {
        WechatUser oldWebchatUser = wechatUserRepository.findByOpenId(wechatUser.getOpenId());
        if (null != oldWebchatUser) {
            log.info("updateUserInfo UnionId:"+wechatUser.getUnionId());
            oldWebchatUser.setCity(wechatUser.getCity());
            oldWebchatUser.setProvince(wechatUser.getProvince());
            oldWebchatUser.setSex(wechatUser.getSex());
            oldWebchatUser.setCountry(wechatUser.getCountry());
            oldWebchatUser.setImgUrl(wechatUser.getImgUrl());
            oldWebchatUser.setLanguage(wechatUser.getLanguage());
            oldWebchatUser.setNick(wechatUser.getNick());
            oldWebchatUser.setLastOpenDate(new Date());
            oldWebchatUser.setUnionId(wechatUser.getUnionId());
            if( null != wechatUser.getAssignAddressTime()){
                oldWebchatUser.setAssignAddressTime(wechatUser.getAssignAddressTime());
            }

            return wechatUserRepository.save(oldWebchatUser);
        } else {
            return null;
        }
    }

    public void delByOpenId(String openId) {
        WechatUser wechatUser = wechatUserRepository.findByOpenId(openId);
        if (null != wechatUser) {
            wechatUserRepository.delete(wechatUser.getId());
        }
    }

    public void delById(String id){
        wechatUserRepository.delete(id);
    }

    /**
     * 通过用户id list获取 微信用户列表
     *
     * @param userIdList
     * @return
     */
    public List<WechatUser> findWechatUserByUserIdList(List<String> userIdList) {
        if (ListUtils.checkListIsNotNull(userIdList)) {
            Criteria criteria = new Criteria().andOperator(
                    Criteria.where("userId").in(userIdList));
            Query query = new Query(criteria);
            return userMongoTemplate.find(query, WechatUser.class);
        }
        return null;
    }

    public WechatUser findByOpenId(String openId) {
        return wechatUserRepository.findByOpenId(openId);
    }

    public WechatUser findByUnionId(String unionId) {
        return wechatUserRepository.findByUnionId(unionId);
    }

    public Page<WechatUser> findAll(Integer page, Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "subscribeTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return wechatUserRepository.findAll(pageRequest);
    }

    public Page<WechatUser> findByNickLike(String nick, Integer page, Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "subscribeTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return wechatUserRepository.findByNickLike(nick, pageRequest);
    }



    public void unsubscribe(String openId) {
        log.info("unsubscribe,openId:{}",openId);
        WechatUser wechatUser = this.findByOpenId(openId);
        wechatUser.setSubscribeState(1);
        wechatUserRepository.save(wechatUser);
        wechatUserLogService.save(openId, "unsubscribe");
    }

    public List<WechatUser> findNewUser(Date beginDate, Date endDate) {
        Query query = new Query();
        Criteria criteria = Criteria.where("createTime").gte(beginDate).lte(endDate);
        return  userMongoTemplate.find(query,WechatUser.class);
    }

    public long countBySubscribeTimeBetween(long from,long to){
        return wechatUserRepository.countBySubscribeTimeBetween(from,to);
    }
    public Page<WechatUser> findBySubscribeTimeBetween(long from,long to,int page,int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "subscribeTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return wechatUserRepository.findBySubscribeTimeBetween(from,to, pageRequest);
    }



    public List<WechatUser> findAll(){
        return wechatUserRepository.findAll();
    }

    public WechatUser findByUserId(String userId){
        return wechatUserRepository.findByUserId(userId);
    }

    public List<WechatUser> findByIds(List<String> idList){
        return wechatUserRepository.findByIdIn(idList);
    }




}
