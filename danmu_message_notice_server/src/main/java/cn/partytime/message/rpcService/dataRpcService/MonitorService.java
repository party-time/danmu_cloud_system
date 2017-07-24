package cn.partytime.message.rpcService.dataRpcService;

import cn.partytime.common.util.ServerConst;
import cn.partytime.message.model.Monitor;
import cn.partytime.message.rpcService.dataRpcService.impl.MonitorServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by administrator on 2017/7/24.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = MonitorServiceHystrix.class)
public interface MonitorService {

    @RequestMapping(value = "/rpcMonitor/findById" ,method = RequestMethod.GET)
    public Monitor findById(@RequestParam(value = "id")  String id);


    @RequestMapping(value = "/rpcMonitor/findByKey" ,method = RequestMethod.GET)
    public Monitor findByKey(@RequestParam(value = "id")  String id);


}
