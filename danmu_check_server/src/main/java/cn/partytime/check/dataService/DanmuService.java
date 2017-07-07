package cn.partytime.check.dataService;

import cn.partytime.check.dataService.impl.DanmuLogServiceHystrix;
import cn.partytime.check.dataService.impl.DanmuServiceHystrix;
import cn.partytime.check.model.DanmuModel;
import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by dm on 2017/7/6.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = DanmuServiceHystrix.class)
public interface DanmuService {

    @RequestMapping(value = "/danmu/findById" ,method = RequestMethod.GET)
    public DanmuModel findById(@RequestParam(value = "id") String id);


    @RequestMapping(value = "/danmu/save" ,method = RequestMethod.POST)
    public DanmuModel save(DanmuModel danmuModel);


    @RequestMapping(value = "/danmu/findDanmuByIsBlocked" ,method = RequestMethod.GET)
    public List<DanmuModel> findDanmuByIsBlocked(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size, @RequestParam(value = "isBlocked") boolean isBlocked);
}
