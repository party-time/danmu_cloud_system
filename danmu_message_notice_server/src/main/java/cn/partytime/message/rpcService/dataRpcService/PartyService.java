package cn.partytime.message.rpcService.dataRpcService;


import cn.partytime.common.util.ServerConst;
import cn.partytime.message.rpcService.dataRpcService.impl.PartyServiceHystrix;
import cn.partytime.model.PartyDTO;
import cn.partytime.model.PartyLogicModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = PartyServiceHystrix.class)
public interface PartyService {

    @RequestMapping(value = "/rpcParty/findPartyAddressId" ,method = RequestMethod.GET)
    public PartyLogicModel findPartyAddressId(@RequestParam(value = "addressId") String addressId);

    @RequestMapping(value = "/rpcParty/getPartyByPartyId" ,method = RequestMethod.GET)
    public PartyDTO getPartyByPartyId(@RequestParam(value = "partyId") String partyId);


}
