package cn.partytime.rpcService.dataRpcService;

import cn.partytime.common.util.ServerConst;
import cn.partytime.rpcService.dataRpcService.impl.DanmuAddressServiceHystrix;
import cn.partytime.model.DanmuAddress;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = DanmuAddressServiceHystrix.class)
public interface DanmuAddressService {

    @RequestMapping(value = "/rpcDanmuAddress/findById" ,method = RequestMethod.GET)
    public DanmuAddress findById(@RequestParam(value = "id") String id);

    @RequestMapping(value = "/rpcDanmuAddress/findAddressListByType" ,method = RequestMethod.GET)
    public List<DanmuAddress> findByType(@RequestParam(value = "type") Integer type);

}
