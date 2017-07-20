package cn.partytime.collector.rpcService.dataRpcService;

import cn.partytime.collector.rpcService.dataRpcService.impl.DanmuAddressServiceHystrix;
import cn.partytime.collector.model.DanmuAddress;
import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/5.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = DanmuAddressServiceHystrix.class)
public interface DanmuAddressService {


    @RequestMapping(value = "/rpcDanmuAddress/findAddressByLonLat" ,method = RequestMethod.GET)
    public DanmuAddress findAddressByLonLat(@RequestParam(value = "longitude") Double longitude, @RequestParam(value = "latitude") Double latitude);

}
