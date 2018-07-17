package cn.partytime.rpc;

import cn.partytime.cache.user.WechatUserCountCacheService;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.common.util.LocalDateTimeUtils;
import cn.partytime.logicService.DanmuAddressLogicService;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.model.danmu.PayDanmu;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.model.wechat.WechatUserInfo;
import cn.partytime.model.wechat.WechatUserWeekCount;
import cn.partytime.service.wechat.WechatUserInfoService;
import cn.partytime.service.wechat.WechatUserService;
import cn.partytime.service.wechat.WechatUserWeekCountService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by dm on 2017/7/10.
 */

@Slf4j
@RestController
@RequestMapping("/rpcWechat")
public class RpcWechatService {

    @Autowired
    private WechatUserInfoService wechatUserInfoService;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private WechatUserWeekCountService wechatUserWeekCountService;


    @Autowired
    private DanmuAddressLogicService danmuAddressLogicService;

    @Autowired
    private WechatUserCountCacheService wechatUserCountCacheService;



    @RequestMapping(value = "/findByWechatId" ,method = RequestMethod.GET)
    public WechatUserInfo findByWechatId(@RequestParam String wechatId){
        return wechatUserInfoService.findByWechatId(wechatId);
    }

    @RequestMapping(value = "/findByOpenId" ,method = RequestMethod.GET)
    public WechatUser findByOpenId(@RequestParam String openId) {
        return wechatUserService.findByOpenId(openId);
    }


    @RequestMapping(value = "/findByUserId" ,method = RequestMethod.GET)
    public WechatUser findByUserId(@RequestParam String userId) {
        return wechatUserService.findByUserId(userId);
    }

    @RequestMapping(value = "/findByRegistDateInRange" ,method = RequestMethod.GET)
    public List<WechatUserInfo> findByRegistDateInRange(@RequestParam Date startDate, @RequestParam Date endDate ) {
        return wechatUserInfoService.findByRegistDateBetween(startDate,endDate);
    }

    @RequestMapping(value = "/countNewWechatUser" ,method = RequestMethod.GET)
    public void countNewWechatUser() {

        //System.out.println("rpc===========start=====================:"+DateUtils.dateToString(startDate,"yyyy-MM-dd HH:mm:ss"));
        //return wechatUserWeekCountService.findByAddressIdAndStartDateAndEndDate(addressId,startDate,endDate);

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

        List<WechatUserInfo> wechatUserInfoDtoList = wechatUserInfoService.findByRegistDateBetween(startDate,endDate);

        if(ListUtils.checkListIsNotNull(wechatUserInfoDtoList)){
            log.info("上周注册的用户数量：{}",wechatUserInfoDtoList.size());
            wechatUserInfoDtoList.forEach(wechatUserInfo -> addAddressUser(wechatUserInfo));
            Set<String> stringSet = wechatUserCountCacheService.getWechatUserAddress();
            if(stringSet!=null){
                for(String addressId:stringSet){
                    //DanmuAddressModel danmuAddressModel = rpcDanmuAddressService.findById(addressId);
                    Object object = wechatUserCountCacheService.getWechatUserCount(addressId);
                    int count = IntegerUtils.objectConvertToInt(object);
                    System.out.println("start=====================:"+DateUtils.dateToString(startDate,"yyyy-MM-dd HH:mm:ss"));

                    WechatUserWeekCount wechatUserWeekCount = wechatUserWeekCountService.findByAddressIdAndStartDateAndEndDate(addressId,startDate,endDate);

                    if(wechatUserWeekCount==null){
                        wechatUserWeekCount = new WechatUserWeekCount();
                    }

                    wechatUserWeekCount.setAddressId(addressId);
                    wechatUserWeekCount.setStartDate(startDate);
                    wechatUserWeekCount.setEndDate(endDate);
                    wechatUserWeekCount.setCount(count);
                    wechatUserWeekCountService.save(wechatUserWeekCount);
                }
            }
        }else{
            log.info("上周没有注册新用户");
        }

    }


    @RequestMapping(value = "/saveWechatUserWeekCount" ,method = RequestMethod.POST)
    public void saveWechatUserWeekCount(@RequestBody WechatUserWeekCount wechatUserWeekCount) {
        wechatUserWeekCountService.save(wechatUserWeekCount);
    }


    public void addAddressUser(WechatUserInfo wechatUserInfo){

        if(wechatUserInfo!=null){
            Double registLongitude = wechatUserInfo.getLastLongitude();
            Double registLatitude = wechatUserInfo.getRegistLatitude();
            try{
                DanmuAddress danmuAddress = danmuAddressLogicService.findAddressByLonLat(registLongitude,registLatitude);
                log.info("danmuAddressModel:{}",JSON.toJSONString(danmuAddress));
                if(danmuAddress!=null){
                    String addressId = danmuAddress.getId();
                    wechatUserCountCacheService.addWechatUser(addressId);
                    wechatUserCountCacheService.setWechatUserAddress(addressId);
                }else{
                    wechatUserCountCacheService.addWechatUser("0");
                    wechatUserCountCacheService.setWechatUserAddress("0");
                }
            }catch (Exception e){
                log.info("============{}",JSON.toJSONString(wechatUserInfo));
            }

        }
    }






}
