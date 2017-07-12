package cn.partytime.rpc;

import cn.partytime.common.cachekey.CmdTempCacheKey;
import cn.partytime.logicService.CmdLogicService;
import cn.partytime.model.CmdTempAllData;
import cn.partytime.model.CmdTempComponentData;
import cn.partytime.model.manager.danmuCmdJson.CmdComponent;
import cn.partytime.model.manager.danmuCmdJson.CmdComponentValue;
import cn.partytime.model.manager.danmuCmdJson.CmdJsonParam;
import cn.partytime.model.manager.danmuCmdJson.CmdTemp;
import cn.partytime.redis.service.RedisService;
import cn.partytime.service.danmuCmdJson.CmdComponentValueService;
import cn.partytime.service.danmuCmdJson.CmdJsonComponentService;
import cn.partytime.service.danmuCmdJson.CmdJsonParamService;
import cn.partytime.service.danmuCmdJson.CmdTempService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.dc.pr.PRError;

import java.util.ArrayList;
import java.util.List;

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


    @RequestMapping(value = "/findCmdTempAllDataById" ,method = RequestMethod.GET)
    public CmdTempAllData findCmdTempAllDataById(@RequestParam String id){
       return cmdLogicService.findCmdTempAllDataById(id);
    }
}
