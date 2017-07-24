package cn.partytime.rpcService.dataRpc;

import cn.partytime.rpcService.dataRpc.impl.PartyServiceHystrix;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/6.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = PartyServiceHystrix.class)
public interface PartyService {

    @RequestMapping(value = "/rpcParty/findByWechatId" ,method = RequestMethod.GET)
    public PartyLogicModel findPartyByLonLat(@RequestParam(value = "longitude") Double longitude, @RequestParam(value = "latitude") Double latitude);


    @RequestMapping(value = "/rpcParty/findPartyAddressId" ,method = RequestMethod.GET)
    public PartyLogicModel findPartyAddressId(@RequestParam(value = "addressId") String addressId);


    @RequestMapping(value = "/rpcParty/getPartyDmDensity" ,method = RequestMethod.GET)
    public int getPartyDmDensity(@RequestParam(value = "addressId")  String addressId, @RequestParam(value = "partyId") String partyId);
}
