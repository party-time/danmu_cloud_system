package cn.partytime.service;

import cn.partytime.model.OperationLogResult;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.manager.AdminUser;
import cn.partytime.model.operationlog.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class BmsOperationLogService {

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private AdminUserService adminUserService;

    public PageResultModel findAllLog(Integer pageNumber, Integer pageSize){
        Page<OperationLog> operationLogPage = operationLogService.findAll(pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        if( null != operationLogPage && null != operationLogPage.getContent()){
            List<String> adminUserIdList = new ArrayList<>();
            for(OperationLog operationLog : operationLogPage.getContent()){
                adminUserIdList.add(operationLog.getAdminUserId());
            }
            List<AdminUser> adminUserList = null;
            if( adminUserIdList.size() > 0){
                adminUserList = adminUserService.findByIds(adminUserIdList);
            }
            List<OperationLogResult> operationLogResultList = new ArrayList<>();
            if( null != adminUserList){
                for(OperationLog operationLog : operationLogPage.getContent()){
                    OperationLogResult operationLogResult = new OperationLogResult();
                    operationLogResult.setOperationLog(operationLog);
                    for(AdminUser adminUser : adminUserList){
                        if(!StringUtils.isEmpty(operationLog.getAdminUserId())
                                && operationLog.getAdminUserId().equals(adminUser.getId())){
                            operationLogResult.setAdminUser(adminUser);
                        }
                    }
                    operationLogResultList.add(operationLogResult);
                }
            }

            pageResultModel.setTotal(operationLogPage.getTotalElements());
            pageResultModel.setRows(operationLogResultList);
        }
        return pageResultModel;
    }

}
