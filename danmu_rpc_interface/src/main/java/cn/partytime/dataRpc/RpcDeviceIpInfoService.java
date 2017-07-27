package cn.partytime.dataRpc;

import cn.partytime.dataRpc.impl.RpcDeviceIpInfoServiceHystrix;
import cn.partytime.model.DeviceIpInfoModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by dm on 2017/7/12.
 */
@FeignClient(name = "${dataRpcServer}",fallback = RpcDeviceIpInfoServiceHystrix.class)
public interface RpcDeviceIpInfoService {

    @RequestMapping(value = "/rpcDeviceIpInfo/findByAddressId" ,method = RequestMethod.GET)
    public List<DeviceIpInfoModel> findByAddressId(@RequestParam(value = "addressId") String addressId);
}
