package cn.partytime.rpcService.dataRpcService;

import cn.partytime.common.util.ServerConst;
import cn.partytime.model.Party;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.rpcService.dataRpcService.impl.PartyServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/19.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = PartyServiceHystrix.class)
public interface PartyService {

    @RequestMapping(value = "/rpcParty/findPartyAddressId" ,method = RequestMethod.GET)
    public PartyLogicModel findPartyAddressId(@RequestParam(value = "addressId") String addressId);




    @RequestMapping(value = "/rpcParty/findByMovieAliasOnLine" ,method = RequestMethod.GET)
    public Party findByMovieAliasOnLine(@RequestParam(value = "command") String command);



    @RequestMapping(value = "/rpcParty/getPartyByPartyId" ,method = RequestMethod.GET)
    public Party getPartyByPartyId(@RequestParam(value = "partyId") String partyId);
}
