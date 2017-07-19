package cn.partytime.rpc;

import cn.partytime.common.util.PartyTypeEnmu;
import cn.partytime.logicService.MovieLogicService;
import cn.partytime.logicService.PartyLogicService;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.client.DanmuClient;
import cn.partytime.model.manager.Party;
import cn.partytime.service.DanmuClientService;
import cn.partytime.service.PartyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dm on 2017/7/11.
 */

@RestController
@RequestMapping("/rpcMovie")
public class RpcMovieService {

    private static final Logger logger = LoggerFactory.getLogger(RpcMovieService.class);

    @Autowired
    private MovieLogicService movieLogicService;


    @RequestMapping(value = "/partyStart" ,method = RequestMethod.GET)
    public RestResultModel partyStart(@RequestParam String partyId,@RequestParam String addressId,@RequestParam long clientTime) {

        return movieLogicService.danmuStart(partyId,addressId,clientTime);

    }

    @RequestMapping(value = "/moviceStart" ,method = RequestMethod.GET)
    public RestResultModel moviceStart(@RequestParam String partyId,@RequestParam String addressId,@RequestParam long clientTime) {
        return movieLogicService.movieStart(partyId,addressId,clientTime);
    }

    @RequestMapping(value = "/moviceStop" ,method = RequestMethod.GET)
    public RestResultModel moviceStop(@RequestParam String partyId, @RequestParam String addressId,@RequestParam long clientTime) {
        logger.info("电影结束请求：活动编号：{},registCode:{}", partyId, addressId);
        return movieLogicService.movieStop(partyId,addressId,clientTime);
    }

}
