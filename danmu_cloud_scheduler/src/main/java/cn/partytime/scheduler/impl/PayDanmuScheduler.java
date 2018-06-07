package cn.partytime.scheduler.impl;

import cn.partytime.alarmRpc.RpcPayDanmuAlarmService;
import cn.partytime.business.danmu.DanmuCommandBussinessService;
import cn.partytime.cache.alarm.AlarmCacheService;
import cn.partytime.common.cachekey.admin.AdminUserCacheKey;
import cn.partytime.common.constants.AlarmKeyConst;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.common.util.SetUtils;
import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.dataRpc.RpcMovieScheduleService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.scheduler.BaseScheduler;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by admin on 2018/5/28.
 */

@Service("payDanmuScheduler")
@EnableScheduling
@RefreshScope
@Slf4j
public class PayDanmuScheduler  implements BaseScheduler {

    @Autowired
    private RpcDanmuAddressService rpcDanmuAddressService;

    @Autowired
    private DanmuCommandBussinessService danmuCommandBussinessService;

    @Autowired
    private RpcPayDanmuAlarmService rpcPayDanmuAlarmService;

    @Autowired
    private AlarmCacheService alarmCacheService;


    @Override
    @Scheduled(cron = "0 0/1 * * * ?")
    public void execute() throws IOException {
        log.info("支付弹幕发送失败");
        List<DanmuAddressModel> danmuAddressList = rpcDanmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressList)){
            for(DanmuAddressModel danmuAddressModel:danmuAddressList){

                log.info("场地信息:{}", JSON.toJSONString(danmuAddressModel));
                Date date =   DateUtils.addSecondsToDate(DateUtils.getCurrentDate(),30);
                long score = date.getTime();
                String addressId = danmuAddressModel.getId();
                Set<String> danmuSet = danmuCommandBussinessService.getPayDanmuSendSuccessBeforeScore(addressId,Double.parseDouble(0+""),Double.parseDouble(score+""));
                if(SetUtils.checkSetIsNotNull(danmuSet)){
                    for(String str:danmuSet){
                        log.info("弹幕编号:{}",str);
                        //alarmCacheService.addAlarmCount(0, AdminUserCacheKey.CHECK_AMDIN_CACHE_KEY,typeName);
                        long count = alarmCacheService.findAlarmCount(addressId, AlarmKeyConst.PAYSENDERROR,str);
                        if(count==0){
                            alarmCacheService.addAlarmCount(0,addressId, AlarmKeyConst.PAYSENDERROR,str);
                            rpcPayDanmuAlarmService.paySendErrorAlarm(addressId,str);
                        }
                    }
                }
            }
        }
    }


    @Scheduled(cron = "0 0/1 * * * ?")
    public void paryNotSendError() throws IOException {
        log.info("支付弹幕未发送");
        List<DanmuAddressModel> danmuAddressList = rpcDanmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressList)){
            for(DanmuAddressModel danmuAddressModel:danmuAddressList){

                log.info("场地信息:{}", JSON.toJSONString(danmuAddressModel));
                Date date =   DateUtils.addSecondsToDate(DateUtils.getCurrentDate(),30);
                long score = date.getTime();
                String addressId = danmuAddressModel.getId();
                Set<String> danmuSet = danmuCommandBussinessService.getPayDanmuNotSendQueue(addressId);
                if(SetUtils.checkSetIsNotNull(danmuSet)){
                    for(String str:danmuSet){
                        log.info("弹幕编号:{}",str);
                        //alarmCacheService.addAlarmCount(0, AdminUserCacheKey.CHECK_AMDIN_CACHE_KEY,typeName);
                        long count = alarmCacheService.findAlarmCount(addressId, AlarmKeyConst.PARYNOTSENDERROR,str);
                        if(count==0){
                            alarmCacheService.addAlarmCount(0,addressId, AlarmKeyConst.PARYNOTSENDERROR,str);
                            rpcPayDanmuAlarmService.payNotSendAlarm(addressId,str);
                        }
                    }
                }
            }
        }
    }


    /**
     * 凌晨8点，清除未支付，展示失败的队列
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void clearPayDanmuQueue(){
        List<DanmuAddressModel> danmuAddressList = rpcDanmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressList)){
            for(DanmuAddressModel danmuAddressModel:danmuAddressList){
                String addressId = danmuAddressModel.getId();
                String name = danmuAddressModel.getName();
                log.info("场地:{},发送成功，客户端未返回信息，支付弹幕队列缓存清除",name);
                //Set<String> stringSet =  danmuCommandBussinessService.getPayDanmuSendSuccessQueue(addressId);
                /*if(SetUtils.checkSetIsNotNull(stringSet)){

                }*/
                /*log.info("场地:{},发送失败，支付弹幕队列缓存清除",name);
                stringSet =danmuCommandBussinessService.getPayDanmuNotSendQueue(addressId);
                if(SetUtils.checkSetIsNotNull(stringSet)){

                }*/
            }
        }
    }
}
