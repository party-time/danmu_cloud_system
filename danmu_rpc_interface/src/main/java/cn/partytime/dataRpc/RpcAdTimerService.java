package cn.partytime.dataRpc;

import cn.partytime.dataRpc.impl.RpcAdTimerServiceHystrix;
import cn.partytime.model.AdTimerResource;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */

@FeignClient(name = "${dataRpcServer}",fallback = RpcAdTimerServiceHystrix.class)
public interface RpcAdTimerService {


    @RequestMapping(value = "/rpcAdTimer/findTimerDanmuFileList" ,method = RequestMethod.GET)
    public AdTimerResource findTimerDanmuFileList(@RequestParam(value = "addressId") String addressId, @RequestParam(value = "partyIdList") List<String> partyIdList);
}
