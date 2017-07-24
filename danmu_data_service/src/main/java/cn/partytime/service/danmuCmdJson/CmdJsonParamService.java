package cn.partytime.service.danmuCmdJson;

import cn.partytime.model.manager.danmuCmdJson.CmdJsonParam;
import cn.partytime.repository.manager.danmuCmdJson.CmdJsonParamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by administrator on 2017/5/8.
 */

@Service
@Slf4j
public class CmdJsonParamService {

    @Autowired
    private CmdJsonParamRepository cmdJsonParamRepository;


    public CmdJsonParam save(String cmdJsonTempId, String componentId, String key, String defaultValue , Integer isCheck, Integer type, String checkRule){
        CmdJsonParam param = new CmdJsonParam();
        param.setCmdJsonTempId(cmdJsonTempId);
        param.setType(type);
        param.setCheckRule(checkRule);
        param.setComponentId(componentId);
        param.setKey(key);
        param.setDefaultValue(defaultValue);
        param.setIsCheck(isCheck);
        return cmdJsonParamRepository.save(param);
    }

    public CmdJsonParam save(CmdJsonParam param ){
        return cmdJsonParamRepository.save(param);
    }

    public CmdJsonParam findById(String id){
        return cmdJsonParamRepository.findOne(id);
    }

    public void del(String id){
        cmdJsonParamRepository.delete(id);
    }

    public List<CmdJsonParam> findByIds(List<String> paramIds){
        return cmdJsonParamRepository.findByIdIn(paramIds);
    }

    public List<CmdJsonParam> findByCmdJsonTempId(String cmdJsonTempId){
        return cmdJsonParamRepository.findByCmdJsonTempIdOrderBySortAsc(cmdJsonTempId);
    }

    public void update(String id,String cmdJsonTempId,String componentId, String key, String defaultValue ,Integer isCheck,Integer type,String checkRule){
        CmdJsonParam param = findById(id);
        if(null != param){
            param.setCmdJsonTempId(cmdJsonTempId);
            param.setType(type);
            param.setComponentId(componentId);
            param.setKey(key);
            param.setDefaultValue(defaultValue);
            param.setIsCheck(isCheck);
            param.setCheckRule(checkRule);
            cmdJsonParamRepository.save(param);
        }
    }


    public void delByTemplateId(String templateId){
        List<CmdJsonParam> paramList = findByCmdJsonTempId(templateId);
        if( null != paramList && paramList.size() > 0){
            for(CmdJsonParam param : paramList){
                del(param.getId());
            }
        }
    }
}
