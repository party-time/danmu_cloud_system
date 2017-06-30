package cn.partytime.service.param;

import cn.partytime.model.manager.Param;
import cn.partytime.repository.manager.param.ParamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by administrator on 2017/2/23.
 */
@Service
@Slf4j
public class ParamService {

    @Autowired
    private ParamRepository paramRepository;


    public Param save(String name,Integer valueType,String paramTemplateId,String des,String defaultValue ){
        Param param = new Param();
        param.setName(name);
        param.setValueType(valueType);
        param.setParamTemplateId(paramTemplateId);
        param.setDes(des);
        param.setDefaultValue(defaultValue);
        return paramRepository.save(param);
    }

    public Param save( Param param ){
        return paramRepository.save(param);
    }

    public Param findById(String id){
        return paramRepository.findOne(id);
    }

    public void del(String id){
        paramRepository.delete(id);
    }

    public List<Param> findByIds(List<String> paramIds){
        return paramRepository.findByIdIn(paramIds);
    }

    public List<Param> findByParamTemplateId(String paramTemplateId){
        return paramRepository.findByParamTemplateId(paramTemplateId);
    }

    public void update(String id,String name,Integer valueType,String des,String defaultValue ){
        Param param = findById(id);
        if(null != param){
            param.setName(name);
            param.setValueType(valueType);
            param.setDes(des);
            param.setDefaultValue(defaultValue);
            paramRepository.save(param);
        }
    }


    public void delByTemplateId(String templateId){
        List<Param> paramList = findByParamTemplateId(templateId);
        if( null != paramList && paramList.size() > 0){
            for(Param param : paramList){
                del(param.getId());
            }
        }
    }

}
