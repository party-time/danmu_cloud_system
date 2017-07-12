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
    private DanmuClientService danmuClientService;

    @Autowired
    private PartyLogicService partyLogicService;


    @Autowired
    private PartyService partyService;

    @Autowired
    private MovieLogicService movieLogicService;


    @RequestMapping(value = "/partyStatus" ,method = RequestMethod.GET)
    public RestResultModel partyStatus(@RequestParam String registCode){
        RestResultModel restResultModel = new RestResultModel();
        DanmuClient danmuClient = danmuClientService.findByRegistCode(registCode);
        if (danmuClient == null) {
            logger.info("注册码:{}错误", registCode);
            restResultModel.setResult(404);
            restResultModel.setResult_msg("客户端不存在!");
            return restResultModel;
        }

        String addressId = danmuClient.getAddressId();
        PartyLogicModel partyLogicModel = partyLogicService.findPartyAddressId(addressId);
        if(partyLogicModel!=null){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("partyId",partyLogicModel.getPartyId());
            if(partyLogicModel.getActiveTime()!=null){
                map.put("time",partyLogicModel.getActiveTime().getTime());
            }else{
                map.put("time",0);
            }
            map.put("status",partyLogicModel.getStatus());
            map.put("type", PartyTypeEnmu.getEnName(partyLogicModel.getType()));
            restResultModel.setResult(200);
            restResultModel.setData(map);
            return restResultModel;
        }else{
            logger.info("活动不存在");
            restResultModel.setResult(200);
            return restResultModel;
        }
    }

    /**
     * @param registCode
     * @param command
     * @return
     */
    @RequestMapping(value = "/partyStart" ,method = RequestMethod.GET)
    public RestResultModel partyStart(@RequestParam String registCode,@RequestParam String command,@RequestParam long clientTime) {

        Party party = partyService.findByMovieAliasOnLine(command);

        logger.info("弹幕开始请求：指令编号：{},registCode:{}", command, registCode);
        RestResultModel restResultModel = new RestResultModel();
        DanmuClient danmuClient = danmuClientService.findByRegistCode(registCode);
        restResultModel = checkClientExist(danmuClient,registCode);
        if(restResultModel!=null){
            return restResultModel;
        }
        restResultModel = checkPartyIsOk(party);
        if(restResultModel!=null){
            return restResultModel;
        }
        String addressId = danmuClient.getAddressId();
        return movieLogicService.danmuStart(party.getId(),addressId,clientTime);

    }

    /**
     * 电影开始
     * @param registCode
     * @return
     */
    @RequestMapping(value = "/moviceStart" ,method = RequestMethod.GET)
    public RestResultModel moviceStart(@RequestParam String partyId,@RequestParam String registCode,@RequestParam long clientTime) {
        logger.info("电影开始请求：活动编号：{},registCode:{}", partyId, registCode);
        RestResultModel restResultModel = new RestResultModel();
        DanmuClient danmuClient = danmuClientService.findByRegistCode(registCode);
        restResultModel = checkClientExist(danmuClient,registCode);
        if(restResultModel!=null){
            return restResultModel;
        }
        Party party = partyService.findById(partyId);
        restResultModel = checkPartyIsOk(party);
        if(restResultModel!=null){
            return restResultModel;
        }
        String addressId = danmuClient.getAddressId();
        return movieLogicService.movieStart(party.getId(),addressId,clientTime);
    }


    /**
     * 电影结束
     * @param registCode
     * @return
     */
    @RequestMapping(value = "/moviceStop" ,method = RequestMethod.GET)
    public RestResultModel moviceStop(@RequestParam String partyId, @RequestParam String registCode,@RequestParam long clientTime) {
        logger.info("电影结束请求：活动编号：{},registCode:{}", partyId, registCode);
        RestResultModel restResultModel = new RestResultModel();

        DanmuClient danmuClient = danmuClientService.findByRegistCode(registCode);
        restResultModel = checkClientExist(danmuClient,registCode);
        if(restResultModel!=null){
            return restResultModel;
        }
        Party party = partyService.findById(partyId);
        restResultModel = checkPartyIsOk(party);
        if(restResultModel!=null){
            return restResultModel;
        }
        restResultModel = new RestResultModel();
        String addressId = danmuClient.getAddressId();
        return movieLogicService.movieStop(party.getId(),addressId,clientTime);
    }

    private RestResultModel checkPartyIsOk(Party party){
        RestResultModel restResultModel = new RestResultModel();
        if (party == null) {
            logger.info("电影不存在");
            restResultModel.setResult(404);
            restResultModel.setResult_msg("活动不存在");
            return restResultModel;
        }
        if (party.getType() == 0) {
            logger.info("不是电影场");
            restResultModel.setResult(405);
            restResultModel.setResult_msg("此活动不是电影场");
            return restResultModel;
        }

        if (party.getStatus() == 4) {
            logger.info("电影已经下线");
            restResultModel.setResult(406);
            restResultModel.setResult_msg("活动已经下线");
            return restResultModel;
        }

        return null;
    }


    private RestResultModel checkClientExist(DanmuClient danmuClient,String registCode){
        if (danmuClient == null) {
            RestResultModel restResultModel = new RestResultModel();
            logger.info("注册码:{}错误", registCode);
            restResultModel.setResult(404);
            restResultModel.setResult_msg("客户端不存在!");
            return restResultModel;
        }
        return null;
    }

}
