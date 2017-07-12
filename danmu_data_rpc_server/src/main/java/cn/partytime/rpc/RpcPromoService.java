package cn.partytime.rpc;

import cn.partytime.common.cachekey.CommandCacheKey;
import cn.partytime.common.constants.ProtocolConst;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.client.DanmuClient;
import cn.partytime.redis.service.RedisService;
import cn.partytime.service.DanmuClientService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dm on 2017/7/12.
 */

@RestController
@RequestMapping("/rpcPromo")
public class RpcPromoService {

    private static final Logger logger = LoggerFactory.getLogger(RpcPromoService.class);


    @Autowired
    private DanmuClientService danmuClientService;


    @Autowired
    private RedisService redisService;

    @Autowired

    private RedisTemplate redisTemplate;

    @RequestMapping(value = "/sendPromoCommand" ,method = RequestMethod.GET)
    public RestResultModel sendPromoCommand(@RequestParam String name, @RequestParam String status, @RequestParam String registCode){

        logger.info("宣传片播放:{},状态:{},注册码:{}",name,status,registCode);
        DanmuClient danmuClient = danmuClientService.findByRegistCode(registCode);

        RestResultModel restResultModel = new RestResultModel();
        restResultModel = checkClientExist(danmuClient,registCode);
        if(restResultModel!=null){
            return restResultModel;
        }

        String addressId = danmuClient.getAddressId();


        Map<String,Object> commandObject = new HashMap<String,Object>();
        String key = CommandCacheKey.PUB_COMMAND_PROMOTIONALFILM_CACHE+addressId;

        commandObject.put("type", ProtocolConst.PROTOCOL_PROMOTIONALFILM);

        Map<String,Object> dataMap = new HashMap<String,Object>();

        dataMap.put("status",status);

        dataMap.put("name",name);

        commandObject.put("data",dataMap);

        String message = JSON.toJSONString(commandObject);
        logger.info("发送给服务器的客户端{}", message);
        redisService.set(key, message);
        redisService.expire(key, 60 );
        //通知客户端
        redisTemplate.convertAndSend("party:command", addressId);

        restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        return restResultModel;
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
