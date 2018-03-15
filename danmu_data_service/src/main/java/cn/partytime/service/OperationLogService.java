package cn.partytime.service;

import cn.partytime.model.operationlog.OperationLog;
import cn.partytime.model.operationlog.OperationLogTemp;
import cn.partytime.repository.manager.OperationLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
}
