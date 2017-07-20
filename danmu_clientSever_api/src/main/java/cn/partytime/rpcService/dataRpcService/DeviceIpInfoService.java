package cn.partytime.rpcService.dataRpcService;

import cn.partytime.common.util.ServerConst;
import cn.partytime.rpcService.dataRpcService.impl.DeviceIpInfoServiceHystrix;
import cn.partytime.model.DeviceIpInfo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by dm on 2017/7/12.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = DeviceIpInfoServiceHystrix.class)
public interface DeviceIpInfoService {


    @RequestMapping(value = "/rpcDeviceIpInfo/findByAddressId" ,method = RequestMethod.GET)
    public List<DeviceIpInfo> findByAddressId(@RequestParam(value = "addressId") String addressId);
}
