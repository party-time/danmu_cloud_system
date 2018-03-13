package cn.partytime.rpc;

import cn.partytime.service.ResourceFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/rpcResourceFile")
public class RpcResourceFileService {

    @Autowired
    private ResourceFileService resourceFileService;


    /**
     *删除结束一个月活动的资源文件
     */
    @RequestMapping(value = "/delEndPartyResourceFile" ,method = RequestMethod.GET)
    public void delEndPartyResourceFile(){
        resourceFileService.delEndPartyResourceFile();
    }

}
