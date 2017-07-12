package cn.partytime.dataService;

import cn.partytime.common.util.ServerConst;
import cn.partytime.dataService.impl.DanmuServiceHystrix;
import cn.partytime.dataService.impl.ParamServiceHystrix;
import cn.partytime.model.ParamValueJson;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by dm on 2017/7/11.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = ParamServiceHystrix.class)
public interface ParamService {

    @RequestMapping(value = "/rpcParam/findByRegistCode" ,method = RequestMethod.POST)
    public List<ParamValueJson> findByRegistCode(@RequestParam(value = "code") String code);
}
