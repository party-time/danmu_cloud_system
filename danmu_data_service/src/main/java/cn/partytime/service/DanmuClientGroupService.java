package cn.partytime.service;

import cn.partytime.model.client.DanmuClientGroup;
import cn.partytime.repository.manager.DanmuClientGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuwei on 16/6/15.
 */

@Service
@Slf4j
public class DanmuClientGroupService {

    @Autowired
    private DanmuClientGroupRepository danmuClientGroupRepository;

    public DanmuClientGroup save(DanmuClientGroup danmuClientGroup){
        return danmuClientGroupRepository.insert(danmuClientGroup);
    }

    public void deleteById(String id){
        danmuClientGroupRepository.delete(id);
    }

    public DanmuClientGroup updateById(DanmuClientGroup danmuClientGroup){
        return danmuClientGroupRepository.save(danmuClientGroup);
    }

    public DanmuClientGroup findById(String id){
        return danmuClientGroupRepository.findById(id);
    }


}
