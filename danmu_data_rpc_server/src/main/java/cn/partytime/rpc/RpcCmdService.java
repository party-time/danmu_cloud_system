package cn.partytime.rpc;

import cn.partytime.logicService.CmdLogicService;
import cn.partytime.model.CmdTempAllData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dm on 2017/7/10.
 */

@RestController
@RequestMapping("/rpcCmd")
public class RpcCmdService {


    @Autowired
    private CmdLogicService cmdLogicService;

    @RequestMapping(value = "/findCmdTempAllDataByIdFromCache" ,method = RequestMethod.GET)
    public CmdTempAllData findCmdTempAllDataByIdFromCache(@RequestParam String templateId){
        return cmdLogicService.findCmdTempAllDataByIdFromCache(templateId);
    }

    @RequestMapping(value = "/findCmdTempAllDataByKeyFromCache" ,method = RequestMethod.GET)
    public CmdTempAllData findCmdTempAllDataByKeyFromCache(@RequestParam String key){
        return cmdLogicService.findCmdTempAllDataByKeyFromCache(key);
    }


    @RequestMapping(value = "/findCmdTempAllDataById" ,method = RequestMethod.GET)
    public CmdTempAllData findCmdTempAllDataById(@RequestParam String id){
       return cmdLogicService.findCmdTempAllDataById(id);
    }

    @RequestMapping(value = "/findCmdTempAllDataByKey" ,method = RequestMethod.GET)
    public CmdTempAllData findCmdTempAllDataByKey(@RequestParam String key){
        return cmdLogicService.findCmdTempAllDataByKey(key);
    }
}
