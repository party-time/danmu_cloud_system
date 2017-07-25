package cn.partytime.rpcService.dataRpc;

import cn.partytime.model.PartyDTO;
import cn.partytime.rpcService.dataRpc.impl.PartyServiceHystrix;
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

    @RequestMapping(value = "/rpcParty/getPartyByPartyId" ,method = RequestMethod.GET)
    public PartyDTO findById(@RequestParam(value = "partyId") String partyId);

    @RequestMapping(value = "/rpcParty/saveParty" ,method = RequestMethod.POST)
    public PartyDTO updateParty(PartyDTO party);


}
