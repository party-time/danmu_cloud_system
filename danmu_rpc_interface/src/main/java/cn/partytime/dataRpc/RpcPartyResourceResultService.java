package cn.partytime.dataRpc;

import cn.partytime.dataRpc.impl.RpcPartyResourceResultServiceHystrix;
import cn.partytime.model.PartyResourceResult;
import cn.partytime.model.ResourceFileModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created by dm on 2017/7/11.
 */
@FeignClient(value = "${dataRpcServer}",fallback = RpcPartyResourceResultServiceHystrix.class)
public interface RpcPartyResourceResultService {


    /*@RequestMapping(value = "/rpcPartyResourceResult/findLatelyParty" ,method = RequestMethod.POST)
    public List<PartyResourceResult> findLatelyParty();


    @RequestMapping(value = "/rpcPartyResourceResult/findLatelyPartyByAddressIdAndType" ,method = RequestMethod.POST)
    public List<PartyResourceResult> findLatelyPartyByAddressIdAndType(@RequestParam(value = "addressId") String addressId, @RequestParam(value = "type") Integer type);*/

    @RequestMapping(value = "/rpcPartyResourceResult/findResourceUnderFilm" ,method = RequestMethod.GET)
    public Map<String,List<ResourceFileModel>> findResourceUnderFilm(@RequestParam(value = "partyIdList") List<String> partyIdList);



    @RequestMapping(value = "/rpcPartyResourceResult/findResourceUnderParty" ,method = RequestMethod.GET)
    public Map<String,List<ResourceFileModel>> findResourceUnderParty(@RequestParam(value = "addressId") String addressId, @RequestParam(value = "type") Integer type,@RequestParam(value = "partyIdList") List<String> partyIdList);
}
