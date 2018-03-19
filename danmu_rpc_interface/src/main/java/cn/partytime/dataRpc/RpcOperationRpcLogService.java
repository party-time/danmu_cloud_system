package cn.partytime.dataRpc;

import cn.partytime.dataRpc.impl.RpcMovieScheduleServiceHystrix;
import cn.partytime.dataRpc.impl.RpcOperationRpcLogServiceHystrix;
import cn.partytime.model.ParamValueJsonModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2018/3/16.
 */

@FeignClient(value = "${dataRpcServer}",fallback = RpcOperationRpcLogServiceHystrix.class)
public interface RpcOperationRpcLogService {

    @RequestMapping(value = "/rpcOperationRpcLog/insertOperationLog" ,method = RequestMethod.GET)
    //public List<ParamValueJsonModel> insertOperationLog(@RequestParam(value = "key")String key, @RequestParam(value = "content") Map<String,String> content, @RequestParam(value = "adminUserId") String adminUserId);
    public void insertOperationLog(@RequestParam(value = "cmd") String cmd,@RequestParam(value = "partyId") String partyId,@RequestParam(value = "addressId")String addressId, @RequestParam(value = "adminUserId") String adminUserId);


    @RequestMapping(value = "/rpcOperationRpcLog/insertOperationLogOfParty" ,method = RequestMethod.GET)
    //public List<ParamValueJsonModel> insertOperationLog(@RequestParam(value = "key")String key, @RequestParam(value = "content") Map<String,String> content, @RequestParam(value = "adminUserId") String adminUserId);
    public void insertOperationLogOfParty(@RequestParam(value = "cmd") String cmd,@RequestParam(value = "partyId") String partyId,@RequestParam(value = "addressId")String addressId, @RequestParam(value = "adminUserId") String adminUserId,@RequestParam(value = "content") Map<String,Object> content);

}
