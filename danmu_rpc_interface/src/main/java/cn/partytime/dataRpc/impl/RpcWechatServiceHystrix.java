package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcWechatService;
import cn.partytime.model.WechatUserDto;
import cn.partytime.model.WechatUserInfoDto;
import cn.partytime.model.WechatUserWeekCountDto;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class RpcWechatServiceHystrix implements RpcWechatService {

    @Override
    public WechatUserDto findByOpenId(String openId) {
        return null;
    }

    @Override
    public WechatUserInfoDto findByWechatId(String wechatId) {
        return null;
    }

    @Override
    public WechatUserDto findByUserId(String userId) {
        return null;
    }

    @Override
    public List<WechatUserInfoDto> findByRegistDateInRange(Date startDate, Date endDate) {
        return null;
    }

    @Override
    public void countNewWechatUser() {

    }

}
