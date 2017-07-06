package cn.partytime.service.wechat;

import cn.partytime.model.wechat.WechatUser;
import cn.partytime.model.wechat.WechatUserInfo;
import cn.partytime.repository.user.WechatUserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * Created by administrator on 2017/4/13.
 */
@Controller
@RequestMapping("/wechatUserInfo")
public class WechatUserInfoService {

    @Autowired
    private WechatUserInfoRepository wechatUserInfoRepository;
    @Autowired
    private WechatUserService wechatUserService;

    public void saveOrUpdate(String wechatId,Double longitude,Double latitude){
        WechatUserInfo wechatUserInfo = wechatUserInfoRepository.findByWechatId(wechatId);
        if( null == wechatUserInfo){
            wechatUserInfo = new WechatUserInfo();
            wechatUserInfo.setRegistLatitude(latitude);
            wechatUserInfo.setRegistLongitude(longitude);
            wechatUserInfo.setRegistDate(new Date());
        }else{
            if( null == wechatUserInfo.getRegistLatitude() || null == wechatUserInfo.getRegistLongitude()){
                wechatUserInfo.setRegistLatitude(latitude);
                wechatUserInfo.setRegistLongitude(longitude);
                wechatUserInfo.setRegistDate(new Date());
            }
        }
        wechatUserInfo.setWechatId(wechatId);
        wechatUserInfo.setLastLatitude(latitude);
        wechatUserInfo.setLastLongitude(longitude);
        wechatUserInfo.setLastGetLocationDate(new Date());
        wechatUserInfoRepository.save(wechatUserInfo);
    }

    public void updateLastOpenDate(String wechatId){
        WechatUserInfo wechatUserInfo = wechatUserInfoRepository.findByWechatId(wechatId);
        wechatUserInfo.setLastGetLocationDate(new Date());
        wechatUserInfoRepository.save(wechatUserInfo);
    }

    @RequestMapping(value = "/findByWechatId" ,method = RequestMethod.GET)
    public WechatUserInfo findByWechatId(@RequestParam String wechatId){
        return wechatUserInfoRepository.findByWechatId(wechatId);
    }

    public List<WechatUserInfo> findByWechatIds(List<String> wechatIdList){
        return wechatUserInfoRepository.findByWechatIdIn(wechatIdList);
    }

    public void initWechatUserInfo(){

        List<WechatUser> wechatUserList = wechatUserService.findAll();
        for( WechatUser wechatUser : wechatUserList){

            WechatUserInfo wechatUserInfo = new WechatUserInfo();
            wechatUserInfo.setWechatId(wechatUser.getId());
            wechatUserInfo.setRegistDate(wechatUser.getCreateDate());
            wechatUserInfo.setRegistLatitude(wechatUser.getLatitude());
            wechatUserInfo.setRegistLongitude(wechatUser.getLongitude());

            wechatUserInfo.setLastLongitude(wechatUser.getLongitude());
            wechatUserInfo.setLastLatitude(wechatUser.getLatitude());
            wechatUserInfo.setLastGetLocationDate(wechatUser.getGetLocationTime());

            wechatUserInfoRepository.save(wechatUserInfo);

        }

    }

    public void delbyWechatId(String wechatId){
        WechatUserInfo wechatUserInfo = wechatUserInfoRepository.findByWechatId(wechatId);
        if( null != wechatUserInfo){
            wechatUserInfoRepository.delete(wechatUserInfo.getId());
        }
    }

    public void update(WechatUserInfo wechatUserInfo){
        wechatUserInfoRepository.save(wechatUserInfo);
    }


}
