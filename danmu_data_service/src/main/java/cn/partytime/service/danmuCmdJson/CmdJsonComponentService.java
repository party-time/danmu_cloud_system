package cn.partytime.service.danmuCmdJson;

import cn.partytime.model.manager.danmuCmdJson.CmdComponent;
import cn.partytime.repository.manager.danmuCmdJson.CmdJsonComponentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by administrator on 2017/5/8.
 */

@Service
@Slf4j
public class CmdJsonComponentService {

    @Autowired
    private CmdJsonComponentRepository cmdJsonComponentRepository;

    public CmdComponent save(String name,Integer type){
        CmdComponent cmdComponent = new CmdComponent();
        cmdComponent.setName(name);
        cmdComponent.setType(type);
        return cmdJsonComponentRepository.save(cmdComponent);
    }

    public CmdComponent update(String id,String name,Integer type){
        CmdComponent cmdComponent = findById(id);
        if( null != cmdComponent){
            cmdComponent.setName(name);
            cmdComponent.setType(type);
            cmdJsonComponentRepository.save(cmdComponent);
        }

        return cmdComponent;

    }

    public Page<CmdComponent> findAll(Integer page, Integer size){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        return cmdJsonComponentRepository.findAll(pageRequest);
    }

    public void delById(String id){
        cmdJsonComponentRepository.delete(id);
    }

    public List<CmdComponent> findAll(){
        return cmdJsonComponentRepository.findAll();
    }

    public CmdComponent findById(String id){

        return cmdJsonComponentRepository.findOne(id);
    }
}
