package cn.partytime.dataRpc;

import cn.partytime.dataRpc.impl.RpcRealTimeDmAddressServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by administrator on 2018/1/3.
 */
@FeignClient(value = "${dataRpcServer}",fallback = RpcRealTimeDmAddressServiceHystrix.class)
public interface RpcRealTimeDmAddressService {

    @RequestMapping(value = "/rpcRealTimeDmAddress/findAllByAddressId" ,method = RequestMethod.GET)
    List<String> findAllByAddressId(@RequestParam(value = "addressId")String addressId);


}
