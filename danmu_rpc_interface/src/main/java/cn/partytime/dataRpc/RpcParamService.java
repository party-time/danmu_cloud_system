package cn.partytime.dataRpc;

import cn.partytime.dataRpc.impl.RpcParamServiceHystrix;
import cn.partytime.model.ParamValueJsonModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */

@FeignClient(value = "${dataRpcServer}",fallback = RpcParamServiceHystrix.class)
public interface RpcParamService {

    @RequestMapping(value = "/rpcParam/findByRegistCode" ,method = RequestMethod.GET)
    public List<ParamValueJsonModel> findByRegistCode(@RequestParam(value = "code") String code);
}
