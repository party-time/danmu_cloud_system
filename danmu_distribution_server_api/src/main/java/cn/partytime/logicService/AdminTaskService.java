package cn.partytime.logicService;

import cn.partytime.model.ResultInfo;
import cn.partytime.model.ServerInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by lENOVO on 2016/11/22.
 */
@Service
public class AdminTaskService {


    @Value("${tasksocket.ip}")
    private String ip;


    @Value("${tasksocket.port}")
    private int port;

    @Value("${filmTasksocket.port}")
    private int filmTaskPort;



    public ResultInfo findTaskServerInfo() {
        ResultInfo resultInfo = new ResultInfo();

        resultInfo.setCode(200);
        resultInfo.setMessage("OK!");
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setIp(ip);
        serverInfo.setPort(port);
        resultInfo.setServerInfo(serverInfo);
        return resultInfo;
    }

    public ResultInfo findFilmTaskServerInfo() {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setCode(200);
        resultInfo.setMessage("OK!");
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setIp(ip);
        serverInfo.setPort(filmTaskPort);
        resultInfo.setServerInfo(serverInfo);
        return resultInfo;
    }
}
