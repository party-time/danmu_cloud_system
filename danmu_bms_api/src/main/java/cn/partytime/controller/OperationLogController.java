package cn.partytime.controller;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.operationlog.OperationLog;
import cn.partytime.model.operationlog.OperationLogTemp;
import cn.partytime.service.BmsOperationLogService;
import cn.partytime.service.OperationLogService;
import cn.partytime.service.OperationLogTempService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/api/admin/operationLog")
@Slf4j
public class OperationLogController {

    @Autowired
    private OperationLogTempService operationLogTempService;

    @Autowired
    private BmsOperationLogService bmsOperationLogService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel findAll(Integer pageNumber, Integer pageSize){
        if( null == pageNumber){
            pageNumber = 0;
        }else{
            pageNumber = pageNumber-1;
        }
        Page<OperationLogTemp> operationLogTempPage = operationLogTempService.findAll(pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(operationLogTempPage.getTotalElements());
        pageResultModel.setRows(operationLogTempPage.getContent());

        return pageResultModel;
    }

    @RequestMapping(value = "/pageLog", method = RequestMethod.GET)
    public PageResultModel findAllLog(Integer pageNumber, Integer pageSize){
        if( null == pageNumber){
            pageNumber = 0;
        }else{
            pageNumber = pageNumber-1;
        }
        return bmsOperationLogService.findAllLog(pageNumber,pageSize);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel save(String title, String adminUserIds, String content, String key , String wechatTempId){
        RestResultModel restResultModel = new RestResultModel();
        if(StringUtils.isEmpty(key)){
            restResultModel.setResult(404);
            return restResultModel;
        }

        if(operationLogTempService.countByKey(key)>0){
            restResultModel.setResult(404);
            restResultModel.setResult_msg("key有重复的");
            return restResultModel;
        }

        OperationLogTemp operationLog = new OperationLogTemp();
        operationLog.setTitle(title);
        operationLog.setContent(content);
        operationLog.setKey(key);
        operationLogTempService.save(operationLog);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResultModel update(String id,String title, String adminUserIds, String content, String key, String wechatTempId){
        RestResultModel restResultModel = new RestResultModel();

        OperationLogTemp operationLog = operationLogTempService.findById(id);
        if( null == operationLog){
            restResultModel.setResult(404);
            restResultModel.setResult_msg("报警不存在");
            return restResultModel;
        }

        if( StringUtils.isEmpty(key)){
            restResultModel.setResult(404);
            restResultModel.setResult_msg("报警的key不存在");
            return restResultModel;
        }else{

            if(!key.equals(operationLog.getKey())){
                if(operationLogTempService.countByKey(key)>0){
                    restResultModel.setResult(501);
                    restResultModel.setResult_msg("key有重复,请重新填写");
                    return restResultModel;
                }
            }
        }
        operationLog.setId(id);
        operationLog.setTitle(title);
        operationLog.setContent(content);
        operationLog.setKey(key);
        operationLogTempService.update(operationLog);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/countByKey", method = RequestMethod.GET)
    public RestResultModel countByKey(String key){
        RestResultModel restResultModel = new RestResultModel();
        Integer count = operationLogTempService.countByKey(key);
        restResultModel.setResult(200);
        restResultModel.setData(count);
        return restResultModel;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RestResultModel get(String id){
        RestResultModel restResultModel = new RestResultModel();
        OperationLogTemp operationLog = operationLogTempService.findById(id);
        restResultModel.setResult(200);
        restResultModel.setData(operationLog);
        return restResultModel;
    }
}
