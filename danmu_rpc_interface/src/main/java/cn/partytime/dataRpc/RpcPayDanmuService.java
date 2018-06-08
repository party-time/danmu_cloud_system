package cn.partytime.dataRpc;

import cn.partytime.dataRpc.impl.RpcPartyServiceHystrix;
import cn.partytime.dataRpc.impl.RpcPayDanmuServiceHystrix;
import cn.partytime.model.DanmuLogModel;
import cn.partytime.model.PayDanmuDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by admin on 2018/6/7.
 */

@FeignClient(value = "${dataRpcServer}",fallback = RpcPayDanmuServiceHystrix.class)
public interface RpcPayDanmuService {



    @RequestMapping(value = "/rpcPayDanmu/save" ,method = RequestMethod.POST)
    public PayDanmuDto findDanmuLogById(@RequestParam(value = "id") String id);



    @RequestMapping(value = "/rpcPayDanmu/findByDanmuId" ,method = RequestMethod.POST)
    public void save(PayDanmuDto payDanmuDto);


}
