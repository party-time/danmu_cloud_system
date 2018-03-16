package cn.partytime.logicService;

import cn.partytime.model.operationlog.OperationLog;
import cn.partytime.model.operationlog.OperationLogTemp;
import cn.partytime.service.OperationLogService;
import cn.partytime.service.OperationLogTempService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class OperationRpcLogService {

    @Autowired
    private OperationLogTempService operationLogTempService;

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 新增操作日志
     * @param key
     * @param content
     */
    public void insertOperationLog(String key,Map<String,String> content,String adminUserId){

        OperationLogTemp operationLogTemp = operationLogTempService.findByKey(key);
        if( null == operationLogTemp){
            throw new IllegalArgumentException("操作日志的key不存在");
        }

        String patternString = "\\$\\{(" + StringUtils.join(content.keySet(), "|") + ")\\}";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(operationLogTemp.getContent());
        //两个方法：appendReplacement, appendTail
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(sb, content.get(matcher.group(1)));
        }
        matcher.appendTail(sb);

        String msg = sb.toString();

        OperationLog operationLog = new OperationLog();
        operationLog.setAdminUserId(adminUserId);
        operationLog.setCreateTime(new Date());
        operationLog.setMessage(msg);
        operationLogService.save(operationLog);

    }
}
