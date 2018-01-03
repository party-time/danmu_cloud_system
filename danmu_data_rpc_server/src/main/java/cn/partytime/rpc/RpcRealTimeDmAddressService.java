package cn.partytime.rpc;

import cn.partytime.service.RealTimeDmAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by administrator on 2018/1/3.
 */
@RestController
@RequestMapping("/rpcRealTimeDmAddress")
public class RpcRealTimeDmAddressService {

    @Autowired
    private RealTimeDmAddressService realTimeDmAddressService;

    @RequestMapping(value = "/findAllByAddressId" ,method = RequestMethod.GET)
    public List<String>  findAllByAddressId(String addressId){
        return realTimeDmAddressService.findAllByAddressId(addressId);
    }


}
