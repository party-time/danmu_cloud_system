package cn.partytime.collector.dataService;

import cn.partytime.collector.dataService.impl.WechatUserInfoServiceHystrix;
import cn.partytime.collector.model.WechatUserInfo;
import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/5.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = WechatUserInfoServiceHystrix.class)
public interface WechatUserInfoService {

    @RequestMapping(value = "/wechatUserInfoService/findByWechatId" ,method = RequestMethod.GET)
    public WechatUserInfo findByWechatId(@RequestParam(value = "wechatId") String wechatId);
}