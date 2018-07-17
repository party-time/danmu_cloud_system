package cn.partytime.controller;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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

        /*LocalDateTime now = LocalDateTime.now();

        LocalDateTime localDateTimeTemp = LocalDateTimeUtils.minu(now,
                5,
                ChronoUnit.DAYS);


        Date tempDate = LocalDateTimeUtils.convertLDTToDate(localDateTimeTemp);

        LocalDateTime endLocalDateTime = LocalDateTimeUtils.minu(now,
                1,
                ChronoUnit.DAYS);
        LocalDateTime startLocalDateTime = LocalDateTimeUtils.minu(now,
                1000,
                ChronoUnit.DAYS);
        Date startDate = LocalDateTimeUtils.convertLDTToDate(LocalDateTimeUtils.getDayStart(startLocalDateTime));

        Date endDate = LocalDateTimeUtils.convertLDTToDate(LocalDateTimeUtils.getDayEnd(endLocalDateTime));

        List<WechatUserInfo>  wechatUserInfoList = wechatUserInfoService.findByRegistDateBetween(startDate,endDate);
        //wechatUserInfoList.forEach(wechatUserInfo -> wechatUserInfo.setRegistDate(tempDate));
        for(WechatUserInfo wechatUserInfo:wechatUserInfoList){
            wechatUserInfo.setRegistDate(tempDate);
            wechatUserInfoService.update(wechatUserInfo);
        }*/



        PageResultModel pageResultModel = new PageResultModel();
        Page<WechatUserWeekCount> wechatUserWeekCountPage =  wechatUserWeekCountService.findAll(pageNumber,pageSize);
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
