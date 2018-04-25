package cn.partytime.controller.wechat;

import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.WechatSession;
import cn.partytime.model.manager.FastDanmu;
import cn.partytime.model.manager.H5Template;
import cn.partytime.model.manager.ResourceFile;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.model.wechat.WechatUserInfo;
import cn.partytime.service.*;
import cn.partytime.service.wechat.WechatUserInfoService;
import cn.partytime.service.wechat.WechatUserService;
import cn.partytime.util.FileUploadUtil;
import cn.partytime.util.WeixinUtil;
import cn.partytime.wechat.pojo.UserInfo;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by admin on 2018/4/25.
 */


@Slf4j
@RestController
@RequestMapping(value = "/v1/api/wechatMini")
public class WechatMiniRestController {

    @Autowired
    private BmsDanmuService bmsDanmuService;

    @Autowired
    private ResourceFileService resourceFileService;

    @Autowired
    private BmsWechatUserService bmsWechatUserService;

    @Autowired
    private WechatUserInfoService wechatUserInfoService;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private BmsColorService bmsColorService;

    @Autowired
    private FastDanmuService fastDanmuService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @RequestMapping(value = "/findPartyInfo", method = RequestMethod.POST)
    public RestResultModel partyInfo(HttpServletRequest request) {
        String code  = request.getParameter("code");
        log.info("小程序请求的code:{}",code);
        String openId = WeixinUtil.getUserOpenId(code);
        RestResultModel restResultModel = new RestResultModel();
        WechatUser wechatUser = bmsWechatUserService.findByOpenId(openId);
        log.info("wechatUser:", JSON.toJSONString(wechatUser));
        UserInfo userInfo = WeixinUtil.getUserInfo(bmsWechatUserService.getAccessToken().getToken(), openId);
        /*WechatUserInfo wechatUserInfo = null;
        if( null != wechatUser){
            wechatUserInfo = wechatUserInfoService.findByWechatId(wechatUser.getId());
            if( null != wechatUserInfo && null != wechatUserInfo.getLastGetLocationDate()){
                long a = (new Date().getTime() - wechatUserInfo.getLastGetLocationDate().getTime())/(1000*60*60);
                if(a > 24){
                    restResultModel.setResult(405);
                    restResultModel.setResult_msg("位置授权");
                    return restResultModel;
                }
            }else{
                restResultModel.setResult(405);
                restResultModel.setResult_msg("位置授权");
                return restResultModel;
            }
        }*/
        if( null != userInfo){
            wechatUserService.updateUserInfo(userInfo.toWechatUser());
        }

        PartyLogicModel party = bmsWechatUserService.findPartyByOpenId(openId);
        if( null == party){
            restResultModel.setResult(404);
            restResultModel.setResult_msg("位置授权");
            return restResultModel;
        }


        Map<String, Object> resourceFileModels = resourceFileService.findResourceMapByPartyId(party.getPartyId());

        List<ResourceFile> all = new ArrayList<>();
        List<ResourceFile> expressionconstant = (List<ResourceFile>)resourceFileModels.get("expressionconstant");
        List<ResourceFile> expressions = (List<ResourceFile>)resourceFileModels.get("expressions");

        if( null != expressionconstant){
            all.addAll(expressionconstant);
        }

        if( null != expressions){
            all.addAll(expressions);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("expressions",all );

        if (null != resourceFileModels.get("h5Background")) {
            List reList = (ArrayList) resourceFileModels.get("h5Background");
            if (reList.size() > 0) {
                map.put("background", reList.get(0));
            }
        }
        map.put("colors", bmsColorService.findDanmuColor(0));
        map.put("openId", openId);
        map.put("partyId",party.getPartyId());
        map.put("addressId",party.getAddressId());

        List<FastDanmu> fastDanmuList = fastDanmuService.findByPartyId(party.getPartyId());
        if( null != fastDanmuList && fastDanmuList.size() > 0){
            map.put("fastdmList",fastDanmuList);
        }

        String fileUploadUrl = fileUploadUtil.getUrl();
        map.put("baseUrl",fileUploadUrl);
        map.put("openId",openId);



        restResultModel.setResult(200);
        restResultModel.setData(map);
        return restResultModel;
    }
    //@RequestMapping(value = "/partyInfo/{code}", method = RequestMethod.POST)
   // public RestResultModel partyInfo(HttpServletRequest request) {

      //  return null;
       /* String openId  = request.getParameter("openId");
        WechatUser wechatUser = bmsWechatUserService.findByOpenId(openId);
        if( null == wechatUser){
            return "redirect:/htm/noparty.html";
        }
        UserInfo userInfo = WeixinUtil.getUserInfo(bmsWechatUserService.getAccessToken().getToken(), openId);
        WechatUserInfo wechatUserInfo = null;
        if( null != wechatUser){
            wechatUserInfo = wechatUserInfoService.findByWechatId(wechatUser.getId());
            if( null != wechatUserInfo && null != wechatUserInfo.getLastGetLocationDate()){
                if( null == wechatUserInfo.getLastLongitude() || null == wechatUserInfo.getLastLatitude()){
                    return "redirect:/wechat/getLocation?openId="+openId;
                }
                long a = (new Date().getTime() - wechatUserInfo.getLastGetLocationDate().getTime())/(1000*60*60);
                if(a > 24){
                    return "redirect:/wechat/getLocation?openId="+openId;
                }
            }else{
                return "redirect:/wechat/getLocation?openId="+openId;
            }
        }
        if( null != userInfo){
            wechatUserService.updateUserInfo(userInfo.toWechatUser());
        }

        PartyLogicModel party = bmsWechatUserService.findPartyByOpenId(openId);
        if( null == party){
            return "redirect:/htm/noparty.html";
        }


        Map<String, Object> resourceFileModels = resourceFileService.findResourceMapByPartyId(party.getPartyId());

        List<ResourceFile> all = new ArrayList<>();
        List<ResourceFile> expressionconstant = (List<ResourceFile>)resourceFileModels.get("expressionconstant");
        List<ResourceFile> expressions = (List<ResourceFile>)resourceFileModels.get("expressions");

        if( null != expressionconstant){
            all.addAll(expressionconstant);
        }

        if( null != expressions){
            all.addAll(expressions);
        }

        model.addAttribute("expressions",all );

        if (null != resourceFileModels.get("h5Background")) {
            List reList = (ArrayList) resourceFileModels.get("h5Background");
            if (reList.size() > 0) {
                model.addAttribute("background", reList.get(0));
            }
        }
        model.addAttribute("colors", bmsColorService.findDanmuColor(0));
        model.addAttribute("openId", openId);
        model.addAttribute("partyId",party.getPartyId());
        model.addAttribute("addressId",party.getAddressId());

        List<FastDanmu> fastDanmuList = fastDanmuService.findByPartyId(party.getPartyId());
        if( null != fastDanmuList && fastDanmuList.size() > 0){
            model.addAttribute("fastdmList",fastDanmuList);
        }

        String fileUploadUrl = fileUploadUtil.getUrl();
        model.addAttribute("baseUrl",fileUploadUrl);

        return ftlName;*/

    //}

    @RequestMapping(value = "/wechartSend", method = RequestMethod.POST)
    public RestResultModel wechartSend(HttpServletRequest request) {
        log.info("小程序端，弹幕发送");
        String openId = request.getParameter("openId");
        RestResultModel restResultModel = new RestResultModel();
        if (bmsDanmuService.checkFrequency(request)) {
            restResultModel.setResult(403);
            restResultModel.setResult_msg("Limited Frequency");
            log.info("用户{}，发送弹幕,太频繁",openId);
            return restResultModel;
        }else if(bmsDanmuService.checkDanmuIsRepeat(openId,request.getParameter("message"))){
            restResultModel.setResult(403);
            restResultModel.setResult_msg("相同弹幕发送太多");
            log.info("用户{}，相同弹幕发送太多",openId);
            return restResultModel;
        }else {
            return bmsDanmuService.sendDanmu(request,openId,0);
        }

    }

    @RequestMapping(value = "/sendExpression", method = RequestMethod.POST)
    public RestResultModel sendExpression(HttpServletRequest request) {
        log.info("小程序端，发送表情");
        RestResultModel restResultModel = new RestResultModel();
        String openId = request.getParameter("openId");
        if (bmsDanmuService.checkFrequency(request)) {
            restResultModel.setResult(403);
            restResultModel.setResult_msg("Limited Frequency");
            log.info("用户{}，发送弹幕,太频繁",openId);
            return restResultModel;
        }else {
            return  bmsDanmuService.sendDanmu(request,openId,0);
        }
    }
}
