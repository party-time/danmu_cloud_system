package cn.partytime.logicService;

import cn.partytime.common.cachekey.CmdTempCacheKey;
import cn.partytime.logic.cmdCommand.CmdTempAllData;
import cn.partytime.logic.cmdCommand.CmdTempComponentData;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dm on 2017/6/7.
 */

@Service
public class CmdLogicService {

    @Autowired
    private CmdTempService cmdTempService;


    @Autowired
    private CmdJsonParamService cmdJsonParamService;


    @Autowired
    private CmdJsonComponentService cmdJsonComponentService;

    @Autowired
    private CmdComponentValueService cmdComponentValueService;

    @Autowired
    private RedisService redisService;



    public CmdTempAllData findCmdTempAllDataByIdFromCache(String templateId){
        String key = CmdTempCacheKey.CMD_TEMP_CACHE_KEY+templateId;
        Object object = redisService.get(key);
        CmdTempAllData cmdTempAllData = null;
        if(object!=null){
            cmdTempAllData = JSON.parseObject(String.valueOf(object),CmdTempAllData.class);
            return cmdTempAllData;
        }else {
            cmdTempAllData = findCmdTempAllDataById(templateId);
            redisService.set(key, JSON.toJSONString(cmdTempAllData));
            redisService.expire(key,24*60*60*3);
        }
        return cmdTempAllData;
    }



    public CmdTempAllData findCmdTempAllDataById(String id){
        CmdTempAllData cmdTempAllData = new CmdTempAllData();
        CmdTemp cmdTemp = cmdTempService.findById(id);
        if( null != cmdTemp){
            cmdTempAllData.setId(cmdTemp.getId());
            cmdTempAllData.setKey(cmdTemp.getKey());
            cmdTempAllData.setName(cmdTemp.getName());
            cmdTempAllData.setIsSendH5(cmdTemp.getIsSendH5());
            cmdTempAllData.setIsInDanmuLib(cmdTemp.getIsInDanmuLib());
        }
        List<CmdJsonParam> cmdJsonParamList = cmdJsonParamService.findByCmdJsonTempId(id);
        if( null != cmdJsonParamList){
            List<CmdTempComponentData> cmdTempComponentDataList = new ArrayList<>();
            for(CmdJsonParam cmdJsonParam : cmdJsonParamList){
                CmdComponent cmdComponent = cmdJsonComponentService.findById(cmdJsonParam.getComponentId());
                CmdTempComponentData cmdTempComponentData = new CmdTempComponentData();
                cmdTempComponentData.setType(cmdJsonParam.getType());
                cmdTempComponentData.setComponentId(cmdJsonParam.getComponentId());
                cmdTempComponentData.setCheckRule(cmdJsonParam.getCheckRule());
                cmdTempComponentData.setDefaultValue(cmdJsonParam.getDefaultValue());
                cmdTempComponentData.setIsCheck(cmdJsonParam.getIsCheck());
                cmdTempComponentData.setKey(cmdJsonParam.getKey());
                cmdTempComponentData.setSort(cmdJsonParam.getSort());
                cmdTempComponentData.setId(cmdJsonParam.getId());
                if( null != cmdComponent){
                    cmdTempComponentData.setComponentType(cmdComponent.getType());
                }
                if(!StringUtils.isEmpty(cmdJsonParam.getCmdJsonTempId()) && cmdJsonParam.getCmdJsonTempId().length() > 1){
                    List<CmdComponentValue> cmdComponentValueList =cmdComponentValueService.findByComponentId(cmdJsonParam.getComponentId());
                    cmdTempComponentData.setCmdComponentValueList(cmdComponentValueList);
                }

                cmdTempComponentDataList.add(cmdTempComponentData);

            }

            cmdTempAllData.setCmdTempComponentDataList(cmdTempComponentDataList);

        }
        return cmdTempAllData;
    }
}
