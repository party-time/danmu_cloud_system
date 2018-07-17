package cn.partytime.scheduler.impl;


import cn.partytime.cache.user.WechatUserCountCacheService;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.common.util.LocalDateTimeUtils;
import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.dataRpc.RpcWechatService;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.model.WechatUserInfoDto;
import cn.partytime.model.WechatUserWeekCountDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service("uerCountScheduler")
@EnableScheduling
@RefreshScope
@Slf4j
public class UserCountScheduler {

    @Autowired
    private RpcWechatService rpcWechatService;

    @Autowired
    private RpcDanmuAddressService rpcDanmuAddressService;


    @Autowired
    private WechatUserCountCacheService wechatUserCountCacheService;



    @Scheduled(cron = "0 1 0 ? * MON")
    public void countUser(){
        log.info("统计上周产生的用户");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endLocalDateTime = LocalDateTimeUtils.minu(now,
                1,
                ChronoUnit.DAYS);
        LocalDateTime startLocalDateTime = LocalDateTimeUtils.minu(now,
                7,
                ChronoUnit.DAYS);
        Date startDate = LocalDateTimeUtils.convertLDTToDate(LocalDateTimeUtils.getDayStart(startLocalDateTime));

        Date endDate = LocalDateTimeUtils.convertLDTToDate(LocalDateTimeUtils.getDayEnd(endLocalDateTime));

        List<WechatUserInfoDto> wechatUserInfoDtoList = rpcWechatService.findByRegistDateInRange(startDate,endDate);

        wechatUserInfoDtoList.forEach(wechatUserInfoDto -> addAddressUser(wechatUserInfoDto));
        Set<String> stringSet = wechatUserCountCacheService.getWechatUserAddress();
        if(stringSet!=null){
            for(String addressId:stringSet){
                //DanmuAddressModel danmuAddressModel = rpcDanmuAddressService.findById(addressId);
                Object object = wechatUserCountCacheService.getWechatUserCount(addressId);
                int count = IntegerUtils.objectConvertToInt(object);
                WechatUserWeekCountDto wechatUserWeekCountDto = rpcWechatService.findByAddressIdAndStartDateAndEndDate(addressId,startDate,endDate);

                if(wechatUserWeekCountDto==null){
                    wechatUserWeekCountDto = new WechatUserWeekCountDto();
                }

                wechatUserWeekCountDto.setAddressId(addressId);
                wechatUserWeekCountDto.setStartDate(startDate);
                wechatUserWeekCountDto.setEndDate(endDate);
                wechatUserWeekCountDto.setCount(count);
                rpcWechatService.saveWechatUserWeekCount(wechatUserWeekCountDto);
            }
        }

    }

    public void addAddressUser(WechatUserInfoDto wechatUserInfoDto){

        if(wechatUserInfoDto!=null){
            Double registLongitude = wechatUserInfoDto.getRegistLongitude();
            Double registLatitude = wechatUserInfoDto.getRegistLongitude();
            DanmuAddressModel danmuAddressModel = rpcDanmuAddressService.findAddressByLonLat(registLongitude,registLatitude);
            if(danmuAddressModel!=null){
                String addressId = danmuAddressModel.getId();
                wechatUserCountCacheService.addWechatUser(addressId);
                wechatUserCountCacheService.setWechatUserAddress(addressId);
            }else{
                wechatUserCountCacheService.addWechatUser("0");
                wechatUserCountCacheService.setWechatUserAddress("0");
            }
        }
    }
}
