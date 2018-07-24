package cn.partytime.controller;

import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.LocalDateTimeUtils;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.WechatUserWeekCountModel;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.model.wechat.WechatUserInfo;
import cn.partytime.model.wechat.WechatUserWeekCount;
import cn.partytime.service.DanmuAddressService;
import cn.partytime.service.wechat.WechatUserInfoService;
import cn.partytime.service.wechat.WechatUserWeekCountService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Slf4j
@RestController
@RequestMapping(value = "/v1/api/admin/wechatCount")
public class WechatCountController {


    @Autowired
    private WechatUserWeekCountService wechatUserWeekCountService;

    @Autowired
    private DanmuAddressService danmuAddressService;

    @Autowired
    private WechatUserInfoService wechatUserInfoService;

    @RequestMapping(value = "/currentYearCountDate", method = RequestMethod.GET)
    public RestResultModel currentYearCountDate(){

        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(200);

        LocalDate local = LocalDate.now();
        LocalDateTime startLocalDateTime = LocalDateTime.of(local.getYear(), 1, 1, 0, 0,0);
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTimeUtils.convertDateToLDT(Date.from(local.atStartOfDay().atZone(zone).toInstant()));
        Date startDate = LocalDateTimeUtils.convertLDTToDate(LocalDateTimeUtils.getDayEnd(startLocalDateTime));
        Date endDate = LocalDateTimeUtils.convertLDTToDate(LocalDateTimeUtils.getDayEnd(localDateTime));

        List<WechatUserWeekCount> wechatUserWeekCountList = wechatUserWeekCountService.findAll(startDate,endDate);
        Set<String> set = new HashSet<>();
        for(WechatUserWeekCount wechatUserWeekCount:wechatUserWeekCountList){
            String date = DateUtils.dateToString(wechatUserWeekCount.getStartDate(),"yyyy-MM-dd");
            set.add(date);

            set.add("2018-07-09");
        }
        restResultModel.setData(set);
        return restResultModel;
    }


    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel findAll(int pageNumber, int pageSize,HttpServletRequest request){

        //DanmuAddress danmuAddress =  danmuAddressService.findById("5a4d9c04e2f0d248cd43f412");


        /*WechatUserInfo wechatUserInfo = wechatUserInfoService.findByWechatId("5b56e909e6e9b841696f1966");

        wechatUserInfo.setRegistDate(DateUtils.strToDate("2018-07-17 12:25:15","yyyy-MM-dd HH:mm:ss"));
        wechatUserInfoService.update(wechatUserInfo);

        wechatUserInfo = wechatUserInfoService.findByWechatId("5b4f07f0e6e9b872c2f457b7");

        wechatUserInfo.setRegistDate(DateUtils.strToDate("2018-07-17 12:25:15","yyyy-MM-dd HH:mm:ss"));
        wechatUserInfoService.update(wechatUserInfo);

        wechatUserInfo = wechatUserInfoService.findByWechatId("5b4f07f0e6e9b872c2f457b7");

        wechatUserInfo.setRegistDate(DateUtils.strToDate("2018-07-17 12:25:15","yyyy-MM-dd HH:mm:ss"));
        wechatUserInfoService.update(wechatUserInfo);

        wechatUserInfo = wechatUserInfoService.findByWechatId("5af94d16e6e9b8031a28e960");
        wechatUserInfo.setRegistDate(DateUtils.strToDate("2018-07-17 12:25:15","yyyy-MM-dd HH:mm:ss"));
        wechatUserInfoService.update(wechatUserInfo);

        wechatUserInfo = wechatUserInfoService.findByWechatId("5af4128be6e9b8031a28cf1b");

        wechatUserInfo.setRegistDate(DateUtils.strToDate("2018-07-17 12:25:15","yyyy-MM-dd HH:mm:ss"));
        wechatUserInfoService.update(wechatUserInfo);*/


        /*System.out.println("================================="+JSON.toJSONString(wechatUserInfo));

        wechatUserInfo.setRegistLatitude(34.586868);
        wechatUserInfo.setRegistLongitude(133.481247307);
        wechatUserInfoService.update(wechatUserInfo);*/


        String dateStr = request.getParameter("date");

        ZoneId zone = ZoneId.systemDefault();

        LocalDate local = LocalDate.now();//获取当前时间
        if(!StringUtils.isEmpty(dateStr)){
            LocalDateTime localDateTime = LocalDateTimeUtils.convertDateToLDT(DateUtils.strToDate(dateStr+" 00:00:00","yyyy-MM-dd HH:mm:ss"));
            local = localDateTime.toLocalDate();
        }

        //LocalDate local = LocalDate.now();//获取当前时间
        DayOfWeek dayOfWeek = local.getDayOfWeek();//获取今天是周几
        LocalDate mondayLocalDate = local.minusDays(7+dayOfWeek.getValue()-1);//算出上周一
        LocalDate weekendLocalDateMoring = local.minusDays(dayOfWeek.getValue());//算出上周一

        LocalDateTime WeekendLocalDateTime = LocalDateTimeUtils.convertDateToLDT(Date.from(weekendLocalDateMoring.atStartOfDay().atZone(zone).toInstant()));
        Date startDate = Date.from(mondayLocalDate.atStartOfDay().atZone(zone).toInstant());
        Date endDate = LocalDateTimeUtils.convertLDTToDate(LocalDateTimeUtils.getDayEnd(WeekendLocalDateTime));




        PageResultModel pageResultModel = new PageResultModel();
        Page<WechatUserWeekCount> wechatUserWeekCountPage =  wechatUserWeekCountService.findAllByPage(startDate,endDate,pageNumber-1,pageSize);
        long count  = wechatUserWeekCountPage.getTotalElements();
        pageResultModel.setTotal(count);
        if(count>0){
            List<WechatUserWeekCountModel> wechatUserWeekCountModelList = new ArrayList<>();
            List<WechatUserWeekCount> wechatUserWeekCountList =  wechatUserWeekCountPage.getContent();
            for(WechatUserWeekCount wechatUserWeekCount:wechatUserWeekCountList){
                WechatUserWeekCountModel wechatUserWeekCountModel = new WechatUserWeekCountModel();
                BeanUtils.copyProperties(wechatUserWeekCount,wechatUserWeekCountModel);
                String addressId = wechatUserWeekCount.getAddressId();
                DanmuAddress danmuAddress =  danmuAddressService.findById(addressId);
                if("0".equals(addressId)){
                    wechatUserWeekCountModel.setAddressName("没有找到匹配场地");
                }else if("1".equals(addressId)){
                    wechatUserWeekCountModel.setAddressName("其他");
                }else{
                    wechatUserWeekCountModel.setAddressName(danmuAddress.getName());
                }

                wechatUserWeekCountModelList.add(wechatUserWeekCountModel);
            }
            pageResultModel.setRows(wechatUserWeekCountModelList);
        }
        return pageResultModel;
    }

}
