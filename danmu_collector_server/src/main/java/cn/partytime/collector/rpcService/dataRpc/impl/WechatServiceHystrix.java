package cn.partytime.collector.rpcService.dataRpc.impl;

import cn.partytime.collector.rpcService.dataRpc.WechatService;
import cn.partytime.collector.model.WechatUser;
import cn.partytime.collector.model.WechatUserInfo;
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
