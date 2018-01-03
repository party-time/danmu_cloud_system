package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcRealTimeDmAddressService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by administrator on 2018/1/3.
 */
@Component
public class RpcRealTimeDmAddressServiceHystrix implements RpcRealTimeDmAddressService {

    @Override
    public List<String> findAllByAddressId(String addressId){
        return null;
    }
}
