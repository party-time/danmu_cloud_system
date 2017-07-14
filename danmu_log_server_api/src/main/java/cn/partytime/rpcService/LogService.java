package cn.partytime.rpcService;

import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.FileUtils;
import cn.partytime.controller.LogController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by Administrator on 2017/3/18 0018.
 */

@Service
public class LogService {

    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @Value("${client.logDirectory}")
    private String logDirectory;


    public void appendLogToFile(int type,String addressId,String param){


       String directoryName = DateUtils.dateToString(DateUtils.getCurrentDate(),"yyyy-MM-dd");
       String dateStr = DateUtils.dateToString(DateUtils.getCurrentDate(),"yyyy-MM-dd HH:mm:ss");

       String typeStr = "";
       if(type==0){
            typeStr="javalog";
       }else{
           typeStr="flashlog";
        }
       String filePath = logDirectory+File.separator+typeStr+File.separator+directoryName;
       File file = new File(filePath);
       if(!file.exists()){
           file.mkdirs();
       }

       String fileName = filePath+File.separator+addressId;
        logger.info("======================param:"+param);
       StringBuffer stringBuffer  = new StringBuffer();
        stringBuffer.append("time:");
        stringBuffer.append(dateStr);
        stringBuffer.append("\t");
        stringBuffer.append("content:");
        stringBuffer.append(param);
        stringBuffer.append("\n");
        FileUtils.appendContentToFile(fileName,stringBuffer.toString());
    }




}
