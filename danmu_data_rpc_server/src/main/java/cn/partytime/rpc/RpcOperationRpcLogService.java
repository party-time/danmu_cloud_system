package cn.partytime.rpc;

import cn.partytime.logicService.OperationRpcLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by admin on 2018/3/16.
 */

@RestController
@RequestMapping("/rpcOperationRpcLog")
@Slf4j
public class RpcOperationRpcLogService {

    @Autowired
    private OperationRpcLogService operationRpcLogService;

    @RequestMapping(value = "/insertOperationLog" ,method = RequestMethod.GET)
    public void insertOperationLog(@RequestParam String key, @RequestParam Map<String,String> content, @RequestParam String adminUserId){
        operationRpcLogService.insertOperationLog(key,content,adminUserId);
    }
}
