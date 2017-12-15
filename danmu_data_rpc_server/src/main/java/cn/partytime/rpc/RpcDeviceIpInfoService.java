package cn.partytime.rpc;

import cn.partytime.model.manager.DeviceIpInfo;
import cn.partytime.service.DeviceIpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by dm on 2017/7/12.
 */

@RestController
@RequestMapping("/rpcDeviceIpInfo")
public class RpcDeviceIpInfoService {


    @Autowired
    private DeviceIpInfoService deviceIpInfoService;

    @RequestMapping(value = "/findByAddressId" ,method = RequestMethod.GET)
    public List<DeviceIpInfo> findByAddressId(@RequestParam String addressId){
        return deviceIpInfoService.findByAddressId(addressId);
    }
}
