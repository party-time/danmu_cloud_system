package cn.partytime.service;

import cn.partytime.model.client.DanmuClientLog;
import cn.partytime.repository.manager.DanmuClientLogRepository;
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
public class DanmuClientLogService {

    @Autowired
    private DanmuClientLogRepository danmuClientLogRepository;

    public DanmuClientLog save(DanmuClientLog danmuClientLog){
        return danmuClientLogRepository.save(danmuClientLog);
    }

    public void deleteById(String id){
        danmuClientLogRepository.delete(id);
    }

    public DanmuClientLog updateById(DanmuClientLog danmuClientLog){
        return danmuClientLogRepository.save(danmuClientLog);
    }

    public DanmuClientLog findById(String id){
        return danmuClientLogRepository.findById(id);
    }

    public Page<DanmuClientLog> findAll(int page , int size){
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        PageRequest pageRequest = new PageRequest(page,size,sort);
        return danmuClientLogRepository.findAll(pageRequest);
    }

}
