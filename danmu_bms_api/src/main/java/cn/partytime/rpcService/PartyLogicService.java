package cn.partytime.rpcService;

import cn.partytime.common.util.ServerConst;
import cn.partytime.rpcService.dataRpcService.impl.PartyLogicServiceHystrix;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.manager.Party;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = PartyLogicServiceHystrix.class)
public interface PartyLogicService {

    @RequestMapping(value = "/rpcParty/findAddressIdListByPartyId" ,method = RequestMethod.GET)
    public List<String> findAddressIdListByPartyId(@RequestParam(value = "partyId") String partyId);


    @RequestMapping(value = "/rpcParty/getPartyByAddressId" ,method = RequestMethod.GET)
    public Party getPartyId(@RequestParam(value = "addressId") String addressId);


    @RequestMapping(value = "/rpcParty/findPartyAddressId" ,method = RequestMethod.GET)
    public PartyLogicModel findPartyAddressId(@RequestParam(value = "addressId") String addressId);


    @RequestMapping(value = "/rpcParty/findPartyByLonLat" ,method = RequestMethod.GET)
    public PartyLogicModel findPartyByLonLat(@RequestParam(value = "longitude") Double longitude, @RequestParam(value = "latitude") Double latitude);


    @RequestMapping(value = "/rpcParty/findTemporaryParty" ,method = RequestMethod.GET)
    public PartyLogicModel findTemporaryParty(@RequestParam(value = "addressId") String addressId);


    @RequestMapping(value = "/rpcParty/checkPartyIsOver" ,method = RequestMethod.POST)
    public boolean checkPartyIsOver(Party party);

    @RequestMapping(value = "/rpcParty/deleteParty" ,method = RequestMethod.GET)
    public void deleteParty(String partyId);
}
