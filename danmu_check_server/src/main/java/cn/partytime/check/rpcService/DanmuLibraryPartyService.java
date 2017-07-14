package cn.partytime.check.rpcService;

import cn.partytime.check.rpcService.impl.DanmuLibraryPartyServiceHystrix;
import cn.partytime.check.model.DanmuLibraryParty;
import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/6.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = DanmuLibraryPartyServiceHystrix.class)
public interface DanmuLibraryPartyService  {

    @RequestMapping(value = "/rpcDanmuLibraryParty/findByPartyId" ,method = RequestMethod.GET)
    public DanmuLibraryParty findByPartyId(@RequestParam(value = "partyId") String partyId);

}
