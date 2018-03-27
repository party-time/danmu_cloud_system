package cn.partytime.rpc;

import cn.partytime.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rpcConfig")
public class RpcConfigService {

    @Autowired
    private ConfigService configService;

    @RequestMapping(value = "/getUploadPath" ,method = RequestMethod.GET)
    public String getUploadPath(){
        return configService.getUploadPath();
    }

    @RequestMapping(value = "/getDownloadPath" ,method = RequestMethod.GET)
    public String getDownloadPath(){
        return configService.getDownloadPath();
    }

    @RequestMapping(value = "/getBackupPath" ,method = RequestMethod.GET)
    public String getBackupPath(){
        return configService.getBackupPath();
    }


}
