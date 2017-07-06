package cn.partytime.collector.dataService.impl;

import cn.partytime.collector.dataService.PartyLogicService;
import cn.partytime.collector.model.PartyLogicModel;
import org.springframework.stereotype.Component;

/**
 * Created by dm on 2017/7/6.
 */

@Component
public class PartyLogicServiceHystrix implements PartyLogicService {
    @Override
    public PartyLogicModel findPartyByLonLat(Double longitude, Double latitude) {
        return null;
    }

    @Override
    public PartyLogicModel findPartyAddressId(String addressId) {
        return null;
    }
}
