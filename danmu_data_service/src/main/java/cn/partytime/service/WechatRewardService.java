package cn.partytime.service;

import cn.partytime.model.manager.WechatReward;
import cn.partytime.repository.manager.WechatRewardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by administrator on 2016/11/29.
 */
@Service
public class WechatRewardService {

    @Autowired
    private WechatRewardRepository wechatRewardRepository;

    public WechatReward save(String partyId , String addressId , String openId, String timestamp, Integer total_fee, String h5TempId ){
        WechatReward wechatReward = new WechatReward();
        wechatReward.setPartyId(partyId);
        wechatReward.setAddressId(addressId);
        wechatReward.setOpenId(openId);
        wechatReward.setTimestamp(timestamp);
        wechatReward.setTotal_fee(total_fee);
        wechatReward.setH5tempId(h5TempId);
        return wechatRewardRepository.save(wechatReward);
    }

    public Page<WechatReward> findAll(int page , int size){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return wechatRewardRepository.findAll(pageRequest);
    }


}
