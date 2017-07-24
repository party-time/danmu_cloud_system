package cn.partytime.rpcService;

import cn.partytime.common.util.ServerConst;
import cn.partytime.model.WechatUser;
import cn.partytime.rpcService.dataRpc.impl.WechatUserServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/5.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = WechatUserServiceHystrix.class)
public interface WechatUserService {

    @RequestMapping(value = "/rpcWechat/findByOpenId" ,method = RequestMethod.GET)
    public WechatUser findByOpenId(@RequestParam(value = "openId") String openId);
}
