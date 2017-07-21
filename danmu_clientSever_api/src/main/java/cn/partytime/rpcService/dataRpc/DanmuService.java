package cn.partytime.rpcService.dataRpc;

import cn.partytime.common.util.ServerConst;
import cn.partytime.rpcService.dataRpc.impl.DanmuServiceHystrix;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created by dm on 2017/7/11.
 */

@FeignClient(value = ServerConst.SERVER_NAME_DATASERVER,fallback = DanmuServiceHystrix.class)
public interface DanmuService {

    @RequestMapping(value = "/rpcDanmu/findHistoryDanmu" ,method = RequestMethod.GET)
    public List<Map<String,Object>> findHistoryDanmu(@RequestParam(value = "partyId") String partyId, @RequestParam(value = "time") int time, @RequestParam(value = "count") int count);
}
