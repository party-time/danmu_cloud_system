package cn.partytime.rpcService.dataRpc.impl;

import cn.partytime.rpcService.WechatUserService;
import cn.partytime.model.WechatUser;
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
