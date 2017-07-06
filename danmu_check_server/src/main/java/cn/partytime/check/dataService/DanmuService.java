package cn.partytime.check.dataService;

import cn.partytime.check.dataService.impl.DanmuLogServiceHystrix;
import cn.partytime.check.dataService.impl.DanmuServiceHystrix;
import cn.partytime.check.model.DanmuModel;
import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/6.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = DanmuServiceHystrix.class)
public interface DanmuService {

    @RequestMapping(value = "/danmu/findById" ,method = RequestMethod.GET)
    public DanmuModel findById(@RequestParam(value = "id") String id);
}
