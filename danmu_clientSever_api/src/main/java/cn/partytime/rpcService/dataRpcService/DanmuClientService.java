package cn.partytime.rpcService.dataRpcService;

import cn.partytime.common.util.ServerConst;
import cn.partytime.rpcService.dataRpcService.impl.DanmuClientServiceHystrix;
import cn.partytime.model.DanmuClient;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */
@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = DanmuClientServiceHystrix.class)
public interface DanmuClientService {

    @RequestMapping(value = "/rpcDanmuClient/findByRegistCode" ,method = RequestMethod.GET)
    public DanmuClient findByRegistCode(@RequestParam(value = "registCode") String registCode);


    @RequestMapping(value = "/rpcDanmuClient/findByAddressId" ,method = RequestMethod.GET)
    public List<DanmuClient> findByAddressId(@RequestParam(value = "addressId") String addressId);

}
