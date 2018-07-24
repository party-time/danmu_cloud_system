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
import cn.partytime.service.DanmuAddressService;
import cn.partytime.service.wechat.WechatUserInfoService;
import cn.partytime.service.wechat.WechatUserService;
import cn.partytime.service.wechat.WechatUserWeekCountService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Autowired
    private DanmuAddressService danmuAddressService;



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
        LocalDate local = LocalDate.now();//获取当前时间
        DayOfWeek dayOfWeek = local.getDayOfWeek();//获取今天是周几
        LocalDate mondayLocalDate = local.minusDays(7+dayOfWeek.getValue()-1);//算出上周一
        LocalDate weekendLocalDateMoring = local.minusDays(dayOfWeek.getValue());//算出上周一
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime WeekendLocalDateTime = LocalDateTimeUtils.convertDateToLDT(Date.from(weekendLocalDateMoring.atStartOfDay().atZone(zone).toInstant()));
        Date startDate = Date.from(mondayLocalDate.atStartOfDay().atZone(zone).toInstant());
        Date endDate = LocalDateTimeUtils.convertLDTToDate(LocalDateTimeUtils.getDayEnd(WeekendLocalDateTime));
        //List<WechatUserInfo> wechatUserInfoDtoList = wechatUserInfoService.findByRegistDateBetween(startDate,endDate);
        List<WechatUser> wechatUserList =  wechatUserService.findByCreateDateBetween(startDate,endDate);

        if(ListUtils.checkListIsNotNull(wechatUserList)){
            log.info("上周注册的用户数量：{}",wechatUserList.size());
            List<DanmuAddress> danmuAddressList = danmuAddressService.findAll();
            danmuAddressList.forEach(danmuAddress -> wechatUserCountCacheService.clearUserCountCacheData(danmuAddress.getId()));

            wechatUserCountCacheService.clearUserCountCacheData("0");

            wechatUserCountCacheService.clearUserCountCacheData("1");

            wechatUserList.forEach(wechatUser -> addAddressUser(wechatUser));
            Set<String> stringSet = wechatUserCountCacheService.getWechatUserAddress();
            log.info("stringSet:{}",JSON.toJSONString(stringSet));
            if(stringSet!=null){
                for(String addressId:stringSet){
                    Object object = wechatUserCountCacheService.getWechatUserCount(addressId);
                    int count = IntegerUtils.objectConvertToInt(object);
                    //System.out.println("start=====================:"+DateUtils.dateToString(startDate,"yyyy-MM-dd HH:mm:ss"));
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


    public void addAddressUser(WechatUser wechatUser){
        log.info("----------用户信息添加到缓存----------------");
        log.info("----------wechatUser:{}----------------",JSON.toJSONString(wechatUser));
        String wechatId = wechatUser.getId();

        WechatUserInfo wechatUserInfo = wechatUserInfoService.findByWechatId(wechatId);
        if(wechatUserInfo!=null){



            Double registLongitude = wechatUserInfo.getLastLongitude();
            Double registLatitude = wechatUserInfo.getRegistLatitude();
            if(registLatitude==null || registLatitude==null){
                //用户关注了，没有经纬度
                wechatUserCountCacheService.addWechatUser("1");
                wechatUserCountCacheService.setWechatUserAddress("1");
            }else{
                log.info("registLongitude:{},registLatitude:{}",registLongitude,registLatitude);
                DanmuAddress danmuAddress = danmuAddressLogicService.findAddressByLonLat(registLongitude,registLatitude);
                log.info("----------------------------------------------------------------");
                log.info("danmuAddressModel:{}",JSON.toJSONString(danmuAddress));
                if(danmuAddress!=null){
                    String addressId = danmuAddress.getId();
                    wechatUserCountCacheService.addWechatUser(addressId);
                    wechatUserCountCacheService.setWechatUserAddress(addressId);
                }else{
                    wechatUserCountCacheService.addWechatUser("0");
                    wechatUserCountCacheService.setWechatUserAddress("0");
                }
            }

            /*try{

            }catch (Exception e){
                e.printStackTrace();
                log.info("============{}",JSON.toJSONString(wechatUserInfo));
            }*/


        }else{
            log.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }
    }






}
