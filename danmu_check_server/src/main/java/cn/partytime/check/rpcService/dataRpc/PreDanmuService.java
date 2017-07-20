package cn.partytime.check.rpcService.dataRpc;

import cn.partytime.check.rpcService.dataRpc.impl.PreDanmuServiceHystrix;
import cn.partytime.check.model.PreDanmuModel;
import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by dm on 2017/7/6.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = PreDanmuServiceHystrix.class)
public interface PreDanmuService {

    @RequestMapping(value = "/rpcRepDanmu/findByPartyId" ,method = RequestMethod.GET)
    public List<PreDanmuModel> findByPartyId(@RequestParam(value = "partyId") String partyId);
}
