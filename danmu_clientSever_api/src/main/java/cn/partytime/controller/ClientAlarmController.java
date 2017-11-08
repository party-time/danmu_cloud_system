package cn.partytime.controller;

import cn.partytime.alarmRpc.RpcDanmuAlarmService;
import cn.partytime.model.RestResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by dm on 2017/7/18.
 */

@RestController
@RequestMapping(value = "/v1/api/javaClient")
@Slf4j
public class ClientAlarmController {

    @Autowired
    private RpcDanmuAlarmService rpcDanmuAlarmService;

    @RequestMapping(value = "/alarm/{code}/{type}/{idd}", method = RequestMethod.GET)
    public RestResultModel danmuNotice(@PathVariable("code") String code, @PathVariable("type") String type, @PathVariable("idd") String idd){
        RestResultModel restResultModel = new RestResultModel();
        rpcDanmuAlarmService.danmuAlarm(type,code,idd);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/alarm/{addressId}/{number}/{type}", method = RequestMethod.GET)
    public void danmuNotice(@PathVariable("addressId") String addressId, @PathVariable("number") Integer number, @PathVariable("type") Integer type){
        log.info("addressId:{},number:{}",addressId,number);
    }

}
