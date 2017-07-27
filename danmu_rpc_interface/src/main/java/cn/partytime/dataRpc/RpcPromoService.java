package cn.partytime.dataRpc;

import cn.partytime.dataRpc.impl.RpcPromoServiceHystrix;
import cn.partytime.model.RestResultModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/12.
 */
@FeignClient(value = "${dataRpcServer}",fallback = RpcPromoServiceHystrix.class)
public interface RpcPromoService {

    @RequestMapping(value = "/rpcPromo/sendPromoCommand" ,method = RequestMethod.GET)
    public RestResultModel sendPromoCommand(@RequestParam(value = "name") String name, @RequestParam(value = "status") String status, @RequestParam(value = "registCode") String registCode);


}
