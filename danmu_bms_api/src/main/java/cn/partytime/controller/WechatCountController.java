package cn.partytime.controller;

import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.LocalDateTimeUtils;
import cn.partytime.model.PageResultModel;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel findAll(int pageNumber, int pageSize){

        //DanmuAddress danmuAddress =  danmuAddressService.findById("5a4d9c04e2f0d248cd43f412");


        /*WechatUserInfo wechatUserInfo = wechatUserInfoService.findByWechatId("5b56c3e8e6e9b85304c14769");

        wechatUserInfo.setRegistDate(DateUtils.strToDate("2017-07-17 12:25:15","yyyy-MM-dd HH:mm:ss"));
        wechatUserInfoService.update(wechatUserInfo);*/

        /*System.out.println("================================="+JSON.toJSONString(wechatUserInfo));

        wechatUserInfo.setRegistLatitude(34.586868);
        wechatUserInfo.setRegistLongitude(133.481247307);
        wechatUserInfoService.update(wechatUserInfo);*/



        LocalDate local = LocalDate.now();//获取当前时间
        DayOfWeek dayOfWeek = local.getDayOfWeek();//获取今天是周几
        LocalDate mondayLocalDate = local.minusDays(7+dayOfWeek.getValue()-1);//算出上周一
        LocalDate weekendLocalDateMoring = local.minusDays(dayOfWeek.getValue());//算出上周一
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime WeekendLocalDateTime = LocalDateTimeUtils.convertDateToLDT(Date.from(weekendLocalDateMoring.atStartOfDay().atZone(zone).toInstant()));
        Date startDate = Date.from(mondayLocalDate.atStartOfDay().atZone(zone).toInstant());
        Date endDate = LocalDateTimeUtils.convertLDTToDate(LocalDateTimeUtils.getDayEnd(WeekendLocalDateTime));


        PageResultModel pageResultModel = new PageResultModel();
        Page<WechatUserWeekCount> wechatUserWeekCountPage =  wechatUserWeekCountService.findAll(startDate,endDate,pageNumber-1,pageSize);
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
