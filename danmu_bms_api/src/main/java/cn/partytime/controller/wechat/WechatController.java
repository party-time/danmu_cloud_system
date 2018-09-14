package cn.partytime.controller.wechat;

import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.model.DanmuAddressModel;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.WechatSession;
import cn.partytime.model.cms.ItemResult;
import cn.partytime.model.cms.PageColumn;
import cn.partytime.model.manager.*;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.model.wechat.WechatUserInfo;
import cn.partytime.service.*;
import cn.partytime.service.cms.BmsCmsService;
import cn.partytime.service.wechat.WechatUserInfoService;
import cn.partytime.service.wechat.WechatUserService;
import cn.partytime.service.wechatSession.WechatSessionService;
import cn.partytime.util.*;
import cn.partytime.wechat.payService.WechatPayService;
import cn.partytime.wechat.pojo.UserInfo;
import cn.partytime.wechat.pojo.WxJsConfig;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liuwei on 2016/10/10.
 */

@Controller
@RequestMapping(value = "/wechat")
@Slf4j
public class WechatController {

    @Autowired
    private ResourceFileService resourceFileService;

    @Autowired
    private WechatPayService wechatPayService;

    @Autowired
    private BmsWechatUserService bmsWechatUserService;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private PartyTimeConfig partyTimeConfig;

    @Autowired
    private BmsColorService bmsColorService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Autowired
    private WechatSessionService wechatSessionService;

    @Autowired
    private RpcDanmuAddressService danmuAddressLogicService;

    @Autowired
    private WechatUserInfoService wechatUserInfoService;

    @Autowired
    private H5TemplateService h5TemplateService;

    @Autowired
    private H5TempUtil h5TempUtil;

    @Autowired
    private BmsCmsService bmsCmsService;


    @Autowired
    private LovePayService lovePayService;

    @Autowired
    private BmsDanmuService bmsDanmuService;

    @Autowired
    private PartyService partyService;

    @Autowired
    private FastDanmuService fastDanmuService;

    @RequestMapping(value = "/sendDM", method = RequestMethod.GET)
    public String redirectUrl(String code, String partyId, Model model, HttpServletResponse response,HttpServletRequest request){

        log.info("/sendDM:code:{}",code);
        String openId = WeixinUtil.getUserOpenId(code);
        if(StringUtils.isEmpty(openId)){
            return "redirect:/htm/noparty.html";
        }



        WechatUser wechatUser = bmsWechatUserService.findByOpenId(openId);
        if( null == wechatUser){
            return "redirect:/htm/noparty.html";
        }
        if( StringUtils.isEmpty( partyId )) {
            UserInfo userInfo = WeixinUtil.getUserInfo(bmsWechatUserService.getAccessToken().getToken(), openId);
            WechatUserInfo wechatUserInfo = null;
            if (null != wechatUser) {
                wechatUserInfo = wechatUserInfoService.findByWechatId(wechatUser.getId());
                if (null != wechatUserInfo && null != wechatUserInfo.getLastGetLocationDate()) {
                    if (null == wechatUserInfo.getLastLongitude() || null == wechatUserInfo.getLastLatitude()) {
                        return "redirect:/wechat/getLocation?openId=" + openId;
                    }
                    /*
                    long a = (new Date().getTime() -wechatUserInfo.getLastGetLocationDate().getTime())/(1000*60*60);
                    if(a > 24){
                        log.info("######################/sendDM:redirectUrl");
                        return "redirect:/wechat/getLocation?openId="+openId;
                    }*/
                } else {
                    return "redirect:/wechat/getLocation?openId=" + openId;
                }
            }
            if (null != userInfo) {
                wechatUserService.updateUserInfo(userInfo.toWechatUser());
            }
        }
        PartyLogicModel party = null;
        if( !StringUtils.isEmpty(partyId)){
            party = bmsWechatUserService.findPartyByAddressId();
        }else {
            party = bmsWechatUserService.findPartyByOpenId(openId);
        }
        if( null == party){
            return "redirect:/htm/noparty.html";
        }

        WechatSession wechatSession = wechatSessionService.get(openId);

        if( null == wechatSession){
            wechatSession = new WechatSession();
            wechatSession.setOpenId(openId);
            wechatSession.setPartyLogicModel(party);
        }

        wechatSessionService.addSession(wechatSession);


        Map<String, Object> resourceFileModels = resourceFileService.findResourceMapByPartyId(party.getPartyId());

        List<ResourceFile> all = new ArrayList<>();
        List<ResourceFile> expressionconstant = (List<ResourceFile>)resourceFileModels.get("expressionconstant");
        List<ResourceFile> expressions = (List<ResourceFile>)resourceFileModels.get("expressions");

        if( null != expressions){
            all.addAll(expressions);
        }

        if( null != expressionconstant){
            all.addAll(expressionconstant);
        }

        model.addAttribute("expressions",all );

        if (null != resourceFileModels.get("h5Background")) {
            List<ResourceFile> reList = (List<ResourceFile>) resourceFileModels.get("h5Background");
            if (reList.size() > 0) {
                log.info("sendDM :"+reList.get(0).getFilePath());
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
        Cookie cookie = new Cookie("openId", openId);
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        response.addCookie(cookie);

        String ftlName = "sendDM";
        H5Template h5Template = null;
        if(StringUtils.isEmpty(party.getH5TempId())){
            h5Template = h5TemplateService.findByIsBase(0);
        }else{
            h5Template = h5TemplateService.findById(party.getH5TempId());
        }
        if( null != h5Template && !StringUtils.isEmpty(h5Template.getH5Url())){
            ftlName = h5TempUtil.getFtlReadDir()+"/"+h5Template.getH5Url();
        }
        return ftlName;
    }

    @RequestMapping(value = "/h5temp/{h5Url}", method = RequestMethod.GET)
    public String h5temp(@PathVariable("h5Url") String h5Url, @CookieValue(required=false) String openId, String code , String state,Model model, HttpServletRequest request){
        if( StringUtils.isEmpty(openId)){
            openId = WeixinUtil.getUserOpenId(code);
        }
        H5Template h5Template = h5TemplateService.findByH5Url(h5Url);
        if( null != h5Template){
            WxJsConfig wxJsConfig = wechatPayService.createWxjsConfig(request.getRequestURL().toString());
            model.addAttribute("wxJsConfig",wxJsConfig);
            model.addAttribute("openId",openId);
            return h5TempUtil.getFtlReadDir()+"/"+h5Template.getH5Url();
        }
        return "";
    }



    @RequestMapping(value = "/getLocation", method = RequestMethod.GET)
    public String getLocation(String openId, Model model, HttpServletRequest request){
        StringBuffer url = request.getRequestURL();
        String trueUrl = url.toString() + "?openId="+openId;
        String jsTicket = WeixinUtil.getJsTicket(bmsWechatUserService.getAccessToken());
        WxJsConfig wxJsConfig = WechatSignUtil.jsSignature(trueUrl,jsTicket);
        model.addAttribute("wxJsConfig",wxJsConfig);
        model.addAttribute("openId",openId);
        model.addAttribute("appId",partyTimeConfig.getAppId());
        model.addAttribute("url",partyTimeConfig.getUrl());
        return "wechat/getLocation";
    }

    @RequestMapping(value = "/setLocation", method = RequestMethod.GET)
    public String setLocation(String openId , String longitude, String latitude){
        bmsWechatUserService.updateUserLocation(openId,longitude,latitude);
        return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid="+partyTimeConfig.getAppId()+"&redirect_uri="+partyTimeConfig.getUrl()+"/wechat/sendDM&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
    }

    @RequestMapping(value = "/payIndex", method = RequestMethod.GET)
    public String payIndex(String code,String state, Model model, HttpServletRequest request){
        String openId = WeixinUtil.getUserOpenId(code);
        StringBuffer url = request.getRequestURL();
        String trueUrl = url.toString() + "?code="+code+"&state="+state;
        WxJsConfig wxJsConfig = wechatPayService.createWxjsConfig(trueUrl);
        model.addAttribute("opendId",openId);
        model.addAttribute("wxJsConfig",wxJsConfig);
        H5Template h5Template = h5TemplateService.findById("592689fd0cf24d415c21be6a");
        if( null != h5Template){
            return  h5TempUtil.getFtlReadDir()+"/"+h5Template.getH5Url();
        }
        return "wechat/wechatpay";
    }

    @RequestMapping(value = "/lovePayIndex", method = RequestMethod.GET)
    public String lovePayIndex(String code,String state, Model model,String lovePayId, HttpServletRequest request){

        String openId = WeixinUtil.getUserOpenId(code);
        StringBuffer url = request.getRequestURL();
        String trueUrl = url.toString()+"?lovePayId="+lovePayId + "code="+code+"&state="+state;
        WxJsConfig wxJsConfig = wechatPayService.createWxjsConfig(trueUrl);
        model.addAttribute("opendId",openId);
        model.addAttribute("wxJsConfig",wxJsConfig);
        LovePay lovePay = lovePayService.findById(lovePayId);
        if(lovePay.getPrice() == 521){
            model.addAttribute("loveType",1);
        }else if( lovePay.getPrice() == 990){
            model.addAttribute("loveType",2);
        }else if( lovePay.getPrice() == 2100){
            model.addAttribute("loveType",3);
        }else if( lovePay.getPrice() == 5210){
            model.addAttribute("loveType",4);
        }else if(lovePay.getPrice() == 9900 ){
            model.addAttribute("loveType",5);
        }
        model.addAttribute("lovePayId",lovePayId);
        H5Template h5Template = h5TemplateService.findByH5Url("biaobaipay");
        if( null != h5Template){
            return  h5TempUtil.getFtlReadDir()+"/"+h5Template.getH5Url();
        }
        return "wechat/wechatpay";
    }


    @RequestMapping(value = "/buy", method = RequestMethod.GET)
    public String buy(String code,String state, Model model, HttpServletResponse response,@CookieValue(required=false) String openId){
        String openIdStr = null;
        if( StringUtils.isEmpty(code)){
            openIdStr = openId;
        }else{
            openIdStr = WeixinUtil.getUserOpenId(code);
        }

        if( StringUtils.isEmpty(openIdStr)){
            return "redirect:/htm/noshop.html";
        }
        WechatUser wechatUser = bmsWechatUserService.findByOpenId(openIdStr);
        if( null == wechatUser){
            return "redirect:/htm/noshop.html";
        }
        WechatUserInfo wechatUserInfo = wechatUserInfoService.findByWechatId(wechatUser.getId());
        if( null == wechatUserInfo){
            return "redirect:/htm/noshop.html";
        }

        DanmuAddressModel danmuAddress = danmuAddressLogicService.findAddressByLonLat(wechatUserInfo.getLastLongitude(), wechatUserInfo.getLastLatitude());
        if( null == danmuAddress){
            return "redirect:/htm/noshop.html";
        }
        if( null == danmuAddress.getShopStatus() || danmuAddress.getShopStatus() == 1){
            return "redirect:/htm/noshop.html";
        }
        PageColumn pageColumn = bmsCmsService.findItemByAddressId(danmuAddress.getId());
        model.addAttribute("pageColumn",pageColumn);
        model.addAttribute("imgUrl",fileUploadUtil.getUrl());
        model.addAttribute("baseUrl",partyTimeConfig.getUrl());

        Cookie cookie = new Cookie("openId", openIdStr);
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        response.addCookie(cookie);
        log.info("openId:"+openIdStr);

        return "wechat/shop/index";
    }

    @RequestMapping(value = "/item", method = RequestMethod.GET)
    public String item(@CookieValue String openId, Model model, String itemId,HttpServletRequest request){

        log.info("openId:"+openId);
        if( StringUtils.isEmpty(openId)){
            return "redirect:/htm/noshop.html";
        }
        WechatUser wechatUser = bmsWechatUserService.findByOpenId(openId);
        if( null == wechatUser){
            return "redirect:/htm/noshop.html";
        }
        WechatUserInfo wechatUserInfo = wechatUserInfoService.findByWechatId(wechatUser.getId());
        if( null == wechatUserInfo){
            return "redirect:/htm/noshop.html";
        }
        DanmuAddressModel danmuAddress = danmuAddressLogicService.findAddressByLonLat(wechatUserInfo.getLastLongitude(), wechatUserInfo.getLastLatitude());
        if( null == danmuAddress){
            return "redirect:/htm/noshop.html";
        }

        ItemResult itemResult = bmsCmsService.findByItemId(itemId);
        model.addAttribute("itemResult",itemResult);
        if( null != itemResult.getImgList() && itemResult.getImgList().size() > 0){
            model.addAttribute("firstImg",itemResult.getImgList().get(0));
        }else{
            model.addAttribute("firstImg",itemResult.getCoverImg());
        }
        model.addAttribute("baseUrl",partyTimeConfig.getUrl());
        model.addAttribute("imgUrl",fileUploadUtil.getUrl());


        StringBuffer url = request.getRequestURL();
        String trueUrl = url.toString();
        WxJsConfig wxJsConfig = wechatPayService.createWxjsConfig(trueUrl);
        model.addAttribute("opendId",openId);
        model.addAttribute("wxJsConfig",wxJsConfig);

        return "wechat/shop/detail";
    }

    @RequestMapping(value = "/historyDM", method = RequestMethod.GET)
    public String historyDM(String code,Model model,Integer pageNumber,String openId){
        /**
        PageResultModel pageResultModel = bmsDanmuService.findPageResultModel(0,20,"","59917cd2c556f03edcf5259e",1);
        Party party = partyService.findById("59917cd2c556f03edcf5259e");
        model.addAttribute("dataList",pageResultModel.getRows());
        model.addAttribute("total",pageResultModel.getTotal());
        model.addAttribute("pageNumber",pageNumber);
        model.addAttribute("pageSize",pageSize);
        model.addAttribute("party",party);
         **/
        if(StringUtils.isEmpty(openId)){
            openId = WeixinUtil.getUserOpenId(code);
        }
        if(StringUtils.isEmpty(openId)){
            return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid="+partyTimeConfig.getAppId()+"&redirect_uri="+partyTimeConfig.getUrl()+"/wechat/historyDM&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
        }
        PartyLogicModel party = bmsWechatUserService.findPartyByOpenId(openId);
        if( null != party){
            if( null == pageNumber){
                pageNumber = 0;
            }else if(pageNumber>0){
                pageNumber=pageNumber-1;
            }
            model.addAttribute("partyName",party.getPartyName());
            if(!StringUtils.isEmpty(party.getPartyId())){
                PageResultModel pageResultModel = bmsDanmuService.findPageResultDanmuModel(pageNumber,20,party.getAddressId(),party.getPartyId(),1);
                model.addAttribute("dataList",pageResultModel.getRows());
                model.addAttribute("total",pageResultModel.getTotal());
                model.addAttribute("pageNumber",pageNumber);
                long index = 0;
                if (pageResultModel.getTotal() % 20 > 0) {
                    index = pageResultModel.getTotal() / 20 + 1;
                } else {
                    index = pageResultModel.getTotal() / 20;
                }
                model.addAttribute("lastPage",index);
                model.addAttribute("openId",openId);
                model.addAttribute("partyId",party.getPartyId());
                model.addAttribute("addressId",party.getAddressId());
            }else{
                return "redirect:/htm/nodanmu.html";
            }
        }else{
            return "redirect:/htm/nodanmu.html";
        }

        return "wechat/historyDm/historyDm";
    }






}