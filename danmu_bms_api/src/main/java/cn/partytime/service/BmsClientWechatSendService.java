package cn.partytime.service;

import cn.partytime.model.client.DanmuClient;
import cn.partytime.model.manager.AdminUser;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.service.wechat.WechatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by administrator on 2017/3/31.
 */
@Service
public class BmsClientWechatSendService {

    @Autowired
    private BmsWechatUserService bmsWechatUserService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private DanmuClientService danmuClientService;

    @Autowired
    private DanmuAddressService danmuAddressService;

    @Autowired
    private WechatUserService wechatUserService;


    public void sendScreenPic(String registCode,String adminId){
        AdminUser adminUser = adminUserService.findById(adminId);
        if( null != adminUser){
            DanmuClient danmuClient = danmuClientService.findByRegistCode(registCode);
            if( null != danmuClient){
                DanmuAddress danmuAddress =danmuAddressService.findById(danmuClient.getAddressId());

                WechatUser wechatUser = wechatUserService.findById(adminUser.getWechatId());
                if( null != wechatUser){
                    bmsWechatUserService.sendTeamViewTmpl(wechatUser.getOpenId(),danmuAddress.getName(),registCode);
                }
            }

        }
    }
}
