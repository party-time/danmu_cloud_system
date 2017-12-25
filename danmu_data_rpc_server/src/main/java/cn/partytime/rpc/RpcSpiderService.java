package cn.partytime.rpc;

import cn.partytime.model.spider.Spider;
import cn.partytime.service.spider.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by administrator on 2017/12/25.
 */
@RestController
@RequestMapping("/rpcSpider")
public class RpcSpiderService {

    @Autowired
    private SpiderService spiderService;

    @RequestMapping(value = "/saveOrUpdate" ,method = RequestMethod.POST)
    public void saveOrUpdate(List<Spider> spiderList){
        spiderService.saveOrUpDate(spiderList);
    }

}
