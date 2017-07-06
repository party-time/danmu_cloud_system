package cn.partytime.collector.dataService.impl;

import cn.partytime.collector.dataService.WechatUserService;
import cn.partytime.collector.model.WechatUser;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/5.
 */

@Component
public class WechatUserServiceHystrix implements WechatUserService {

    @Override
    public WechatUser findByOpenId(String openId) {
        return null;
    }
}
