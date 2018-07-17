package cn.tst;

import cn.partytime.Application;
import cn.partytime.common.util.LocalDateTimeUtils;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.model.WechatUserInfoDto;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.Party;
import cn.partytime.model.wechat.WechatUserInfo;
import cn.partytime.service.DanmuAddressService;
import cn.partytime.service.PartyService;
import cn.partytime.service.wechat.WechatUserInfoService;
import cn.partytime.service.wechat.WechatUserWeekCountService;
import cn.partytime.wechat.pojo.UserInfo;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/1/23.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes={Application.class,PartyTest.class})
public class PartyTest {


    @Autowired
    private WechatUserInfoService wechatUserInfoService;

    @Autowired
    private DanmuAddressService danmuAddressService;

    @Test
    public void findParty(){


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

        for(WechatUserInfo wechatUserInfo:wechatUserInfoDtoList){
            addAddressUser(wechatUserInfo);
        }

    }

    public void addAddressUser(WechatUserInfo wechatUserInfo){

        if(wechatUserInfo!=null){
            Double registLongitude = wechatUserInfo.getLastLongitude();
            Double registLatitude = wechatUserInfo.getRegistLatitude();
            try{
                List<String> stringList = danmuAddressService.findAddressIdList(registLongitude,registLatitude,2000);
                System.out.println(stringList.get(0));
            }catch (Exception e){
                //log.info("============{}",JSON.toJSONString(wechatUserInfo));
            }

        }
    }
}
