package cn.partytime.collector.dataService;

import cn.partytime.collector.dataService.impl.DanmuClientServiceHystrix;
import cn.partytime.collector.model.DanmuClient;
import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/4.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = DanmuClientServiceHystrix.class)
public interface DanmuClientService {

    @RequestMapping(value = "/rpcDanmuClient/findByRegistCode" ,method = RequestMethod.GET)
    public DanmuClient findByRegistCode(@RequestParam(value = "registCode") String registCode);

}
