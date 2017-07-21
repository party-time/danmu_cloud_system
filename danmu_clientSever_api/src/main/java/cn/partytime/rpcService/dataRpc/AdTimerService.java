package cn.partytime.rpcService.dataRpc;

import cn.partytime.common.util.ServerConst;
import cn.partytime.rpcService.dataRpc.impl.AdTimerServiceHystrix;
import cn.partytime.model.AdTimerResource;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/11.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = AdTimerServiceHystrix.class)
public interface AdTimerService {


    @RequestMapping(value = "/rpcAdTimer/findTimerDanmuFileList" ,method = RequestMethod.GET)
    public AdTimerResource findTimerDanmuFileList(@RequestParam(value = "addressId") String addressId);
}
