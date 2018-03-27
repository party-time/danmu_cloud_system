package cn.partytime.rpc;

import cn.partytime.logicService.DelUpdatePlanFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rpcDelUpdatePlanFile")
@Slf4j
public class RpcDelUpdatePlanFileService {

    @Autowired
    private DelUpdatePlanFileService delUpdatePlanFileService;

    @RequestMapping(value = "/delUpdatePlanFile" ,method = RequestMethod.GET)
    public void delUpdatePlanFile(){
        delUpdatePlanFileService.delUpdatePlanFile();
    }

}
