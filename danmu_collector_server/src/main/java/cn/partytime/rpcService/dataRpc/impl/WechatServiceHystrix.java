package cn.partytime.rpcService.dataRpc.impl;

import cn.partytime.rpcService.dataRpc.WechatService;
import cn.partytime.model.WechatUser;
import cn.partytime.model.WechatUserInfo;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/5.
 */

@Component
public class WechatServiceHystrix implements WechatService {

    @Override
    public WechatUser findByOpenId(String openId) {
        return null;
    }

    @Override
    public WechatUserInfo findByWechatId(String wechatId) {
        return null;
    }
}
