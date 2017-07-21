package cn.partytime.collector.rpcService.dataRpc;

import cn.partytime.collector.model.WechatUser;
import cn.partytime.collector.model.WechatUserInfo;
import cn.partytime.collector.rpcService.dataRpc.impl.WechatServiceHystrix;
import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/5.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = WechatServiceHystrix.class)
public interface WechatService {

    @RequestMapping(value = "/rpcWechat/findByOpenId" ,method = RequestMethod.GET)
    public WechatUser findByOpenId(@RequestParam(value = "openId") String openId);

    @RequestMapping(value = "/rpcWechat/findByWechatId" ,method = RequestMethod.GET)
    public WechatUserInfo findByWechatId(@RequestParam(value = "wechatId") String wechatId);
}
