package cn.partytime.rpcService.dataRpc;


import cn.partytime.common.util.ServerConst;
import cn.partytime.model.PreDanmuModel;
import cn.partytime.rpcService.dataRpc.impl.PartyServiceHystrix;
import cn.partytime.rpcService.dataRpc.impl.PreDanmuServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = PreDanmuServiceHystrix.class)
public interface PreDanmuService {

    @RequestMapping(value = "/rpcRepDanmu/findByPartyId" ,method = RequestMethod.GET)
    public List<PreDanmuModel> findByPartyId(@RequestParam(value = "partyId") String partyId);
}
