package cn.partytime.check.dataService;

import cn.partytime.check.dataService.impl.PartyLogicServiceHystrix;
import cn.partytime.check.dataService.impl.PartyServiceHystrix;
import cn.partytime.check.model.Party;
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

    @RequestMapping(value = "/party/findById" ,method = RequestMethod.GET)
    public Party findById(@RequestParam(value = "partyId") String partyId);


}
