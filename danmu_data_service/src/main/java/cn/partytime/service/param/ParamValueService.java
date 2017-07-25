package cn.partytime.service.param;

import cn.partytime.model.manager.ParamValue;
import cn.partytime.repository.manager.param.ParamValueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by administrator on 2017/2/23.
 */

@Service
@Slf4j
public class ParamValueService {

    @Autowired
    private ParamValueRepository paramValueRepository;

    public ParamValue save(String objId,String paramId,String value){
        ParamValue paramValue = new ParamValue();
        paramValue.setObjId(objId);
        paramValue.setParamId(paramId);
        paramValue.setValue(value);
        return paramValueRepository.save(paramValue);
    }

    public void save(List<ParamValue> paramValueList){
        paramValueRepository.save(paramValueList);
    }

    public List<ParamValue> findByObjId(String objId){
        return paramValueRepository.findByObjId(objId);
    }


    public List<ParamValue> findByObjIdAndTypeAndParamId(String objId,Integer type,String paramId){
        return paramValueRepository.findByObjIdAndTypeAndParamId(objId,type,paramId);
    }

    public List<ParamValue> findByObjIdAndTypeAndParamIdList(String objId,Integer type,List<String> paramIdList){
        return paramValueRepository.findByObjIdAndTypeAndParamIdIn(objId,type,paramIdList);
    }

    public ParamValue findById(String id){
        return paramValueRepository.findOne(id);
    }

    public void del(String id){
        paramValueRepository.delete(id);
    }

    public ParamValue update(String id,String value){
        ParamValue paramValue = findById(id);
        paramValue.setValue(value);
        return paramValueRepository.save(paramValue);
    }


}
