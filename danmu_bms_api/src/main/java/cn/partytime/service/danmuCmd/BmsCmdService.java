package cn.partytime.service.danmuCmd;

import cn.partytime.common.cachekey.CmdTempCacheKey;
import cn.partytime.dataRpc.RpcCmdService;
import cn.partytime.model.CmdTempAllData;
import cn.partytime.model.danmucmd.CmdComponentJson;
import cn.partytime.model.danmucmd.CmdTempJson;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by administrator on 2017/5/8.
 */
@Service
@Slf4j
public class BmsCmdService {



    @Autowired
    private RpcCmdService rpcCmdService;

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

    public void save(CmdTempJson cmdTempJson){
        CmdTemp cmdTemp = null;
        if(StringUtils.isEmpty(cmdTempJson.getCmdTempId())){
            cmdTemp = cmdTempService.save(cmdTempJson.getTempName(),cmdTempJson.getKey(),cmdTempJson.getIsInDanmuLib(),cmdTempJson.getIsSendH5(),cmdTempJson.getSort(),cmdTempJson.getShow());
            cmdTempJson.setCmdTempId(cmdTemp.getId());
        }else{
            cmdTemp = cmdTempService.update(cmdTempJson.getCmdTempId(),cmdTempJson.getTempName(),cmdTempJson.getIsInDanmuLib(),cmdTempJson.getIsSendH5(),cmdTempJson.getSort(),cmdTempJson.getShow());
        }
        if( null != cmdTempJson.getCmdJsonParamList()){
            for(CmdJsonParam cmdJsonParam : cmdTempJson.getCmdJsonParamList()){
                cmdJsonParam.setCmdJsonTempId(cmdTemp.getId());
                cmdJsonParamService.save(cmdJsonParam);
            }
        }

        String key = CmdTempCacheKey.CMD_TEMP_CACHE_ID_KEY+cmdTempJson.getCmdTempId();
        redisService.expire(key,0);
        key = CmdTempCacheKey.CMD_TEMP_CACHE_KEY_KEY+cmdTempJson.getKey();
        redisService.expire(key,0);
    }

    public void saveComponent(CmdComponentJson cmdComponentJson){
        CmdComponent cmdComponent = null;
        if(StringUtils.isEmpty(cmdComponentJson.getComponentId())){
            cmdComponent = cmdJsonComponentService.save(cmdComponentJson.getName(),cmdComponentJson.getType());
        }else{
            cmdComponent =cmdJsonComponentService.update(cmdComponentJson.getComponentId(),cmdComponentJson.getName(),cmdComponentJson.getType());
        }

        if(cmdComponentJson.getCmdComponentValueList() != null){
            for(CmdComponentValue cmdComponentValue : cmdComponentJson.getCmdComponentValueList()){
                cmdComponentValue.setComponentId(cmdComponent.getId());
                cmdComponentValueService.save(cmdComponentValue);
            }
        }
    }

    public CmdTempJson findById(String id){
        CmdTempJson cmdTempJson = new CmdTempJson();
        CmdTemp cmdTemp = cmdTempService.findById(id);
        cmdTempJson.setCmdTempId(cmdTemp.getId());
        cmdTempJson.setTempName(cmdTemp.getName());
        cmdTempJson.setKey(cmdTemp.getKey());
        cmdTempJson.setIsInDanmuLib(cmdTemp.getIsInDanmuLib());
        cmdTempJson.setIsSendH5(cmdTemp.getIsSendH5());
        cmdTempJson.setSort(cmdTemp.getSort());
        cmdTempJson.setShow(cmdTemp.getShow());
        List<CmdJsonParam> cmdJsonParamList = cmdJsonParamService.findByCmdJsonTempId(id);
        cmdTempJson.setCmdJsonParamList(cmdJsonParamList);
        return cmdTempJson;
    }

    public CmdComponentJson findCmdComponentJsonById(String id){
        CmdComponentJson cmdComponentJson = new CmdComponentJson();
        CmdComponent cmdComponent = cmdJsonComponentService.findById(id);
        cmdComponentJson.setName(cmdComponent.getName());
        cmdComponentJson.setType(cmdComponent.getType());
        cmdComponentJson.setComponentId(cmdComponent.getId());
        List<CmdComponentValue> cmdComponentValueList = cmdComponentValueService.findByComponentId(id);
        cmdComponentJson.setCmdComponentValueList(cmdComponentValueList);
        return cmdComponentJson;
    }


}
