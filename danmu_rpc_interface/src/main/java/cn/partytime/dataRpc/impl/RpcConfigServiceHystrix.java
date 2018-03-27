package cn.partytime.dataRpc.impl;

import cn.partytime.dataRpc.RpcConfigService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
public class RpcConfigServiceHystrix implements RpcConfigService {


    public String getUploadPath(){
        return null;
    }

    public String getDownloadPath(){
        return null;
    }

    public String getBackupPath(){
        return null;
    }
}
