package cn.partytime.collector.dataService;

import cn.partytime.collector.dataService.impl.PartyLogicServiceHystrix;
import cn.partytime.collector.model.DanmuAddress;
import cn.partytime.collector.model.PartyLogicModel;
import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/6.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = PartyLogicServiceHystrix.class)
public interface PartyLogicService {

    @RequestMapping(value = "/partyLogicService/findByWechatId" ,method = RequestMethod.GET)
    public PartyLogicModel findPartyByLonLat(@RequestParam(value = "longitude") Double longitude, @RequestParam(value = "latitude") Double latitude);


    @RequestMapping(value = "/partyLogicService/findPartyAddressId" ,method = RequestMethod.GET)
    public PartyLogicModel findPartyAddressId(@RequestParam(value = "addressId") String addressId);
}
