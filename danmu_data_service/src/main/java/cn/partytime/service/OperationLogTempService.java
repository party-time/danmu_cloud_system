package cn.partytime.service;

import cn.partytime.model.operationlog.OperationLogTemp;
import cn.partytime.repository.manager.OperationLogTempRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OperationLogTempService {

    @Autowired
    private OperationLogTempRepository operationLogRepository;

    public OperationLogTemp save(OperationLogTemp operationLog){
        return operationLogRepository.insert(operationLog);
    }

    public OperationLogTemp update(OperationLogTemp operationLog){
        return operationLogRepository.save(operationLog);
    }

    public Page<OperationLogTemp> findAll(int pageNum , int pageSize){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(pageNum, pageSize, sort);
        return operationLogRepository.findAll(pageRequest);
    }

    public OperationLogTemp findById(String id){
        return operationLogRepository.findOne(id);
    }

    public OperationLogTemp findByKey(String key){
        return operationLogRepository.findByKey(key);
    }

    public Integer countByKey(String key){
        return operationLogRepository.countByKey(key);
    }

}
