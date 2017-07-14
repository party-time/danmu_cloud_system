package cn.partytime.rpcService;

import cn.partytime.common.util.ServerConst;
import cn.partytime.rpcService.impl.DanmuAddressServiceHystrix;
import cn.partytime.model.DanmuAddress;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/11.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = DanmuAddressServiceHystrix.class)
public interface DanmuAddressService {

    @RequestMapping(value = "/rpcDanmuAddress/findById" ,method = RequestMethod.GET)
    public DanmuAddress findById(@RequestParam(value = "id") String id);
}
