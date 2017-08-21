package cn.partytime.dataRpc;

import cn.partytime.dataRpc.impl.RpcPreDanmuServiceHystrix;
import cn.partytime.model.PreDanmuModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by dm on 2017/7/10.
 */

@FeignClient(value = "${dataRpcServer}",fallback = RpcPreDanmuServiceHystrix.class)
public interface RpcPreDanmuService {



    @RequestMapping(value = "/rpcRepDanmu/findByPartyId" ,method = RequestMethod.GET)
    public List<PreDanmuModel> findByPartyId(@RequestParam(name = "partyId") String partyId);

}
