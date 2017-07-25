package cn.partytime.service.danmuCmdJson;

import cn.partytime.model.manager.danmuCmdJson.CmdComponentValue;
import cn.partytime.repository.manager.danmuCmdJson.CmdComponentValueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by administrator on 2017/5/8.
 */

@Service
@Slf4j
public class CmdComponentValueService {

    @Autowired
    private CmdComponentValueRepository cmdComponentValueRepository;

    public CmdComponentValue save(String name,String value,String compmnentId){
        CmdComponentValue cmdComponentValue = new CmdComponentValue();
        cmdComponentValue.setName(name);
        cmdComponentValue.setValue(value);
        cmdComponentValue.setComponentId(compmnentId);
        return cmdComponentValueRepository.save(cmdComponentValue);
    }

    public CmdComponentValue save(CmdComponentValue cmdComponentValue){
        return cmdComponentValueRepository.save(cmdComponentValue);
    }

    public List<CmdComponentValue> findByComponentId(String componentId){
        return cmdComponentValueRepository.findByComponentId(componentId);
    }


}
