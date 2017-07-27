package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcWechatService;
import cn.partytime.model.WechatUserDto;
import cn.partytime.model.WechatUserInfoDto;

public class RpcWechatServiceHystrix implements RpcWechatService {

    @Override
    public WechatUserDto findByOpenId(String openId) {
        return null;
    }

    @Override
    public WechatUserInfoDto findByWechatId(String wechatId) {
        return null;
    }
}
