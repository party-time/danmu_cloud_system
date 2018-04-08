package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.model.DanmuAddressModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RpcDanmuAddressServiceHystrix implements RpcDanmuAddressService {

    @Override
    public DanmuAddressModel findAddressByLonLat(Double longitude, Double latitude) {
        return null;
    }

    @Override
    public DanmuAddressModel findById(String id) {
        return null;
    }

    @Override
    public List<DanmuAddressModel> findByType(Integer type) {
        return null;
    }

    @Override
    public void updateAdTime(String id, Integer adTime) {

    }


}
