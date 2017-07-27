package cn.partytime.dataRpc;

import cn.partytime.dataRpc.impl.RpcPartyServiceHystrix;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.PartyModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by dm on 2017/7/10.
 */
@FeignClient(value = "${dataRpcServer}",fallback = RpcPartyServiceHystrix.class)
public interface RpcPartyService {

    @RequestMapping(value = "/rpcParty/findPartyAddressId" ,method = RequestMethod.GET)
    public PartyLogicModel findPartyAddressId(@RequestParam(value = "addressId") String addressId);

    @RequestMapping(value = "/rpcParty/findByMovieAliasOnLine" ,method = RequestMethod.GET)
    public PartyModel findByMovieAliasOnLine(@RequestParam(value = "command") String command);

    @RequestMapping(value = "/rpcParty/getPartyByPartyId" ,method = RequestMethod.GET)
    public PartyModel getPartyByPartyId(@RequestParam(value = "partyId") String partyId);

    @RequestMapping(value = "/rpcParty/findTemporaryParty" ,method = RequestMethod.GET)
    public PartyLogicModel findTemporaryParty(@RequestParam(value = "addressId") String addressId);

    @RequestMapping(value = "/rpcParty/saveParty" ,method = RequestMethod.POST)
    public PartyModel saveParty(PartyModel party);

    @RequestMapping(value = "/rpcParty/findAddressIdListByPartyId" ,method = RequestMethod.GET)
    public List<String> findAddressIdListByPartyId(@RequestParam(value = "partyId") String partyId);



    @RequestMapping(value = "/rpcParty/getPartyByAddressId" ,method = RequestMethod.GET)
    public PartyModel getPartyId(@RequestParam(value = "addressId") String addressId);


    @RequestMapping(value = "/rpcParty/findPartyByLonLat" ,method = RequestMethod.GET)
    public PartyLogicModel findPartyByLonLat(@RequestParam(value = "longitude") Double longitude, @RequestParam(value = "latitude") Double latitude);


    @RequestMapping(value = "/rpcParty/checkPartyIsOver" ,method = RequestMethod.POST)
    public boolean checkPartyIsOver(PartyLogicModel party);

    @RequestMapping(value = "/rpcParty/deleteParty" ,method = RequestMethod.GET)
    public void deleteParty(@RequestParam(value = "partyId") String partyId);

    @RequestMapping(value = "/rpcParty/getPartyDmDensity" ,method = RequestMethod.GET)
    public int getPartyDmDensity(@RequestParam(value = "addressId") String addressId, @RequestParam(value = "partyId") String partyId);


    /**
     *
     * @param type 活动类型 活动:0 电影:1
     * @param status
     * @return
     */
    @RequestMapping(value = "/rpcParty/findByAddressIdAndStatus" ,method = RequestMethod.GET)
    public List<PartyModel> findByTypeAndStatus(@RequestParam(value = "type") Integer type, @RequestParam(value = "status") Integer status);


    @RequestMapping(value = "/rpcParty/findByAddressIdAndStatus" ,method = RequestMethod.GET)
    public List<PartyModel> findByAddressIdAndStatus(@RequestParam(value = "addressId") String addressId, @RequestParam(value = "status") Integer status);

}
