package cn.partytime.service;

import cn.partytime.model.operationlog.OperationLog;
import cn.partytime.model.operationlog.OperationLogTemp;
import cn.partytime.repository.manager.OperationLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class OperationLogService {

    @Autowired
    private OperationLogRepository operationLogRepository;

    public OperationLog save(OperationLog operationLog){
        return operationLogRepository.save(operationLog);
    }

    public Page<OperationLog> findAll(int pageNum , int pageSize){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(pageNum, pageSize, sort);
        return operationLogRepository.findAll(pageRequest);
    }

}
