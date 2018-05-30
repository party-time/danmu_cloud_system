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
    private RpcPartyService rpcPartyService;

    @Autowired
    private AlarmCacheService alarmCacheService;

    @Override
    @Scheduled(cron = "0 0/1 * * * ?")
    public void execute() throws IOException {
        log.info("监控未发送的支付弹幕");
        List<DanmuAddressModel> danmuAddressList = rpcDanmuAddressService.findByType(0);
        if(ListUtils.checkListIsNotNull(danmuAddressList)){
            for(DanmuAddressModel danmuAddressModel:danmuAddressList){

                log.info("场地信息:{}", JSON.toJSONString(danmuAddressModel));
                Date date =   DateUtils.addSecondsToDate(DateUtils.getCurrentDate(),30);
                long score = date.getTime();
                String addressId = danmuAddressModel.getId();
                PartyLogicModel partyLogicModel = rpcPartyService.findPartyByAddressId(addressId);
                if(partyLogicModel!=null){
                    Set<String> danmuSet = danmuCommandBussinessService.getPayDanmuQueueBeforeScore(addressId,Double.parseDouble(0+""),Double.parseDouble(score+""));
                    if(SetUtils.checkSetIsNotNull(danmuSet)){
                        for(String str:danmuSet){
                            log.info("弹幕编号:{}",str);
                            //alarmCacheService.addAlarmCount(0, AdminUserCacheKey.CHECK_AMDIN_CACHE_KEY,typeName);
                            long count = alarmCacheService.findAlarmCount(addressId, AlarmKeyConst.BIAOBAISENDERROR,str);
                            if(count==0){
                                alarmCacheService.addAlarmCount(0,addressId, AlarmKeyConst.BIAOBAISENDERROR,str);
                                rpcPayDanmuAlarmService.biaobaiAlarm(partyLogicModel.getPartyId(),addressId,str);
                            }
                        }
                    }
                }
            }
        }
    }
}
