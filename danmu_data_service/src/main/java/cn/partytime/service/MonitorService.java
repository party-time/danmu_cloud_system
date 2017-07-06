package cn.partytime.service;

import cn.partytime.model.monitor.Monitor;
import cn.partytime.repository.manager.MonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by administrator on 2017/6/20.
 */
@Service
public class MonitorService {

    @Autowired
    private MonitorRepository monitorRepository;


    public Monitor save(Monitor monitor){
        return monitorRepository.insert(monitor);
    }

    public Monitor update(Monitor monitor){
        return monitorRepository.save(monitor);
    }

    public Page<Monitor> findAll(int pageNum , int pageSize){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(pageNum, pageSize, sort);
        return monitorRepository.findAll(pageRequest);
    }

    public Integer countByKey(String key){
        return monitorRepository.countByKey(key);
    }

    public Monitor findById(String id){
        return monitorRepository.findOne(id);

    }



}
