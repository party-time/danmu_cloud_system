package cn.partytime.rpc;

import cn.partytime.logicService.DanmuAddressLogicService;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.service.DanmuAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by dm on 2017/7/10.
 */

@RestController
@RequestMapping("/rpcDanmuAddress")
public class RpcDanmuAddressService {

    private static final Logger logger = LoggerFactory.getLogger(RpcDanmuAddressService.class);

    @Autowired
    private DanmuAddressLogicService danmuAddressLogicService;

    @Autowired
    private DanmuAddressService danmuAddressService;


    @RequestMapping(value = "/findAddressByLonLat" ,method = RequestMethod.GET)
    public DanmuAddress findAddressByLonLat(@RequestParam Double longitude, @RequestParam Double latitude) {
        return danmuAddressLogicService.findAddressByLonLat(longitude,latitude);
    }

    @RequestMapping(value = "/findById" ,method = RequestMethod.GET)
    public DanmuAddress findById(@RequestParam String id) {
        return danmuAddressService.findById(id);
    }


    @RequestMapping(value = "/findAddressListByType" ,method = RequestMethod.GET)
    public List<DanmuAddress> findByType(Integer type){
        return danmuAddressService.findByType(type);
    }


    @RequestMapping(value = "/updateAdTime" ,method = RequestMethod.GET)
    public void  updateAdTime(@RequestParam String id,@RequestParam Integer adTime) {
        danmuAddressService.updateAdTime(id,adTime);
    }

}
