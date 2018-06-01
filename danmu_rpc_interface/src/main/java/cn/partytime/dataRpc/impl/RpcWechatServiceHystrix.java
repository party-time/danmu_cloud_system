package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcWechatService;
import cn.partytime.model.WechatUserDto;
import cn.partytime.model.WechatUserInfoDto;
import org.springframework.stereotype.Component;

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
}
