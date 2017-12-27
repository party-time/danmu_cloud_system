package cn.partytime.controller;

import cn.partytime.alarmRpc.RpcDanmuAlarmService;
import cn.partytime.alarmRpc.RpcJavaClientAlarmService;
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

    @Autowired
    private RpcJavaClientAlarmService rpcJavaClientAlarmService;

    @RequestMapping(value = "/alarm/{code}/{type}/{idd}", method = RequestMethod.GET)
    public RestResultModel danmuNotice(@PathVariable("code") String code, @PathVariable("type") String type, @PathVariable("idd") String idd){
        RestResultModel restResultModel = new RestResultModel();
        rpcDanmuAlarmService.danmuAlarm(type,code,idd);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/alarm/{type}/{addressId}",method = {RequestMethod.GET,RequestMethod.POST})
    public void projectAlaramNotice(@PathVariable("addressId") String addressId, @PathVariable("type") String type){
        log.info("投影仪开启关闭异常告警 场地：{}，类型:{}",type,type);
    }
    @RequestMapping(value = "/alarm",method = {RequestMethod.GET,RequestMethod.POST})
    public void danmuNotice( String addressId,Integer number,String type){
        log.info("addressId:{},number:{},type:{}",addressId,number,type);

        rpcJavaClientAlarmService.javaClientAlarm(type,number,addressId);
    }

}
