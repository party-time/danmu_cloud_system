package cn.partytime.service;

import cn.partytime.model.client.DanmuClientMaster;
import cn.partytime.repository.manager.DanmuClientMasterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by liuwei on 16/6/15.
 */

@Service
@Slf4j
public class DanmuClientMasterService {

    @Autowired
    private DanmuClientMasterRepository danmuClientMasterRepository;

    public DanmuClientMaster save(DanmuClientMaster danmuClientMaster){
        return danmuClientMasterRepository.save(danmuClientMaster);
    }

    public void deleteById(String id){
        danmuClientMasterRepository.delete(id);
    }

    public DanmuClientMaster updateById(DanmuClientMaster danmuClientMaster){
        return danmuClientMasterRepository.save(danmuClientMaster);
    }

    public DanmuClientMaster findById(String id){
        return danmuClientMasterRepository.findById(id);
    }

    public Page<DanmuClientMaster> findAll(int page , int size){
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        PageRequest pageRequest = new PageRequest(page,size,sort);
        return danmuClientMasterRepository.findAll(pageRequest);
    }
}
