package cn.partytime.rpc;

import cn.partytime.model.danmu.PayDanmu;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.model.wechat.WechatUserInfo;
import cn.partytime.model.wechat.WechatUserWeekCount;
import cn.partytime.service.wechat.WechatUserInfoService;
import cn.partytime.service.wechat.WechatUserService;
import cn.partytime.service.wechat.WechatUserWeekCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by dm on 2017/7/10.
 */
@RestController
@RequestMapping("/rpcWechat")
public class RpcWechatService {

    @Autowired
    private WechatUserInfoService wechatUserInfoService;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private WechatUserWeekCountService wechatUserWeekCountService;

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

    @RequestMapping(value = "/findByAddressIdAndStartDateAndEndDate" ,method = RequestMethod.GET)
    public WechatUserWeekCount findByAddressIdAndStartDateAndEndDate(@RequestParam String addressId,@RequestParam Date startDate, @RequestParam Date endDate ) {
        return wechatUserWeekCountService.findByAddressIdAndStartDateAndEndDate(addressId,startDate,endDate);
    }


    @RequestMapping(value = "/saveWechatUserWeekCount" ,method = RequestMethod.POST)
    public void saveWechatUserWeekCount(@RequestBody WechatUserWeekCount wechatUserWeekCount) {
        wechatUserWeekCountService.save(wechatUserWeekCount);
    }






}
