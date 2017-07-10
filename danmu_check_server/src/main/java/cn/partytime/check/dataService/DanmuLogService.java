package cn.partytime.check.dataService;

import cn.partytime.check.dataService.impl.CmdLogicServiceHystrix;
import cn.partytime.check.dataService.impl.DanmuLogServiceHystrix;
import cn.partytime.check.model.CmdTempAllData;
import cn.partytime.check.model.DanmuLog;
import cn.partytime.common.util.ServerConst;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by dm on 2017/7/6.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = DanmuLogServiceHystrix.class)
public interface DanmuLogService {

    @RequestMapping(value = "/rpcDanmu/findDanmuLogById" ,method = RequestMethod.GET)
    public DanmuLog findDanmuLogById(@RequestParam(value = "id") String id);


    @RequestMapping(value = "/rpcDanmu/danmuLogSave" ,method = RequestMethod.POST)
    public DanmuLog save(DanmuLog danmuLog);
}
