package cn.partytime.message.rpcService.dataRpcService;

import cn.partytime.model.WechatUser;

/**
 * Created by administrator on 2017/7/24.
 */
public interface WechatUserService {

    public WechatUser findById(String id);
}
