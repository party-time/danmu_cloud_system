package cn.partytime.controller.wechat;

import cn.partytime.cache.wechatmin.WechatMiniCacheService;
import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.dataRpc.RpcCmdService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.model.*;
import cn.partytime.model.manager.FastDanmu;
import cn.partytime.model.manager.H5Template;
import cn.partytime.model.manager.ResourceFile;
import cn.partytime.model.wechat.UseSecretInfo;
import cn.partytime.model.wechat.WeChatMiniUser;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.model.wechat.WechatUserInfo;
import cn.partytime.service.*;
import cn.partytime.service.wechat.WeChatMiniUserService;
import cn.partytime.service.wechat.WechatUserInfoService;
import cn.partytime.service.wechat.WechatUserService;
import cn.partytime.util.FileUploadUtil;
import cn.partytime.util.WeixinUtil;
import cn.partytime.wechat.payService.WechatPayService;
import cn.partytime.wechat.pojo.UserInfo;
import cn.partytime.wechat.pojo.WxJsConfig;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    private BmsColorService bmsColorService;

    @Autowired
    private FastDanmuService fastDanmuService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Autowired
    private RpcCmdService rpcCmdService;

    @Autowired
    private WechatPayService wechatPayService;

    @Autowired
    private BmsReportService bmsReportService;

    @Autowired
    private RpcPartyService rpcPartyService;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private WechatUserInfoService wechatUserInfoService;

    @Autowired
    private WeChatMiniUserService weChatMiniUserService;

    @Autowired
    private WechatMiniCacheService wechatMiniCacheService;



    @RequestMapping(value = "/wxBingPay", method = RequestMethod.POST)
    public RestResultModel wxBingPay(HttpServletRequest request) {
        RestResultModel restResultModel = new RestResultModel();
        StringBuffer url = request.getRequestURL();
        String openId = request.getParameter("openId");
        String trueUrl = url.toString() + "?&openId="+openId;
        WxJsConfig wxJsConfig = wechatPayService.createWxjsConfig(trueUrl);
        log.info("wxJsConfig:{}",JSON.toJSONString(wxJsConfig));

        String nonceStr = wxJsConfig.getNonceStr();
        String timestamp = wxJsConfig.getTimestamp();
        String body = "弹幕电影-打赏1分";
        String detail="";
        String attach = "";
        Integer total_fee = 1;
        String clientIp = request.getHeader("x-forwarded-for");
        if(StringUtils.isEmpty(clientIp)){
            clientIp = request.getRemoteAddr();
        }
        if(!StringUtils.isEmpty(clientIp) && clientIp.indexOf(",")!=-1){
            clientIp = clientIp.substring(0,clientIp.indexOf(","));
        }
        Map<String,String> map = new HashMap<>();
        map = wechatPayService.createUnifiedorder(nonceStr,timestamp,openId,body,detail,attach,total_fee,clientIp);
        log.info("map:{}",JSON.toJSONString(map));

        restResultModel.setResult(200);
        restResultModel.setData(map);
        return restResultModel;
    }


    @RequestMapping(value = "/findAdvanceTmplate", method = RequestMethod.POST)
    public RestResultModel findAdvanceTmplate(HttpServletRequest request) {
        String key = request.getParameter("key");
        CmdTempAllData cmdTempAllData =  rpcCmdService.findCmdTempAllDataByKeyFromCache(key);
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        restResultModel.setData(cmdTempAllData);
        return restResultModel;
    }



    @RequestMapping(value = "/findPartyInfo", method = RequestMethod.POST)
    public RestResultModel partyInfo(HttpServletRequest request) {
        /*String code  = request.getParameter("code");
        log.info("小程序请求的code:{}",code);


        UseSecretInfo useSecretInfo = WeixinUtil.getUserOpenIdAndSessionKey(code);
        if(useSecretInfo==null){
            //TODO:获取不到用户信息
        }

        RestResultModel restResultModel = new RestResultModel();
        String openId = useSecretInfo.getOpenId();

        //从数据库中获取用户微信信息
        WechatUser wechatUser = bmsWechatUserService.findByOpenId(openId);
        log.info("wechatUser:", JSON.toJSONString(wechatUser));

        //从微信服务器获取用户信息
        UserInfo userInfo = WeixinUtil.getUserInfo(bmsWechatUserService.getAccessToken().getToken(), openId);
        String unionId = userInfo.getUnionid();

        if( null != userInfo){
            wechatUserService.updateUserInfo(userInfo.toWechatUser());
        }*/
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");

        //UseSecretInfo useSecretInfo = WeixinUtil.getMiniProgramUserOpenIdAndSessionKey(code);
        //log.info("useSecretInfo:{}",JSON.toJSONString(useSecretInfo));
       // String openId = useSecretInfo.getOpenId();


        RestResultModel restResultModel = new RestResultModel();
        //UserInfo userInfo = WeixinUtil.getUserInfo(bmsWechatUserService.getAccessToken().getToken(), useSecretInfo.getOpenId());

        //String openId = "oze02wVALzhbkpW9f7r3g036O6vw";
        //PartyLogicModel party = bmsWechatUserService.findPartyByOpenId(openId);
        PartyLogicModel party = rpcPartyService.findPartyByLonLat(Double.parseDouble(longitude+""),Double.parseDouble(latitude+""));
        //log.info("PartyLogicModel:{}",JSON.toJSONString(party));
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
        //map.put("openId", openId);
        map.put("partyId",party.getPartyId());
        map.put("addressId",party.getAddressId());

        List<FastDanmu> fastDanmuList = fastDanmuService.findByPartyId(party.getPartyId());
        if( null != fastDanmuList && fastDanmuList.size() > 0){
            map.put("fastdmList",fastDanmuList);
        }

        String fileUploadUrl = fileUploadUtil.getUrl();
        map.put("baseUrl",fileUploadUrl);
        map.put("partyName",party.getPartyName());
        //map.put("openId",openId);

        //log.info("partyInfo:{}",JSON.toJSONString(map));
        restResultModel.setResult(200);
        restResultModel.setData(map);
        return restResultModel;
    }





    @RequestMapping(value = "/historyDanmu/{openId}/{pageNo}/{pageSize}", method = RequestMethod.GET)
    public PageResultModel historyDanmu(@PathVariable("openId")String openId , @PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize")Integer pageSize) {
        PartyLogicModel party = bmsWechatUserService.findPartyByOpenId(openId);
        log.info("party:{}",JSON.toJSONString(party));
        PageResultModel pageResultModel = bmsDanmuService.findPageResultDanmuModel(pageNo-1,pageSize,party.getAddressId(),party.getPartyId(),1);
        return pageResultModel;
    }

    @RequestMapping(value = "/historyDanmu/report/{openId}/{danmuId}", method = RequestMethod.GET)
    public RestResultModel report(@PathVariable("openId")String openId,@PathVariable("danmuId")String danmuId){
        RestResultModel restResultModel = new RestResultModel();
        String result = bmsReportService.reportDanmu(openId,danmuId);
        if(StringUtils.isEmpty(result)){
            restResultModel.setResult(200);
        }else{
            restResultModel.setResult(500);
            restResultModel.setResult_msg(result);
        }
        return restResultModel;
    }
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RestResultModel login(HttpServletRequest request) {
        RestResultModel restResultModel = new RestResultModel();
        String code  = request.getParameter("code");
        log.info("小程序登陆请求的code:{}",code);
        UseSecretInfo useSecretInfo = WeixinUtil.getMiniProgramUserOpenIdAndSessionKey(code);
        String openId = useSecretInfo.getOpenId();
        String unionId = useSecretInfo.getUnionId();

        WeChatMiniUser weChatMiniUser =  weChatMiniUserService.findByUnionId(unionId);

        log.info("weChatMiniUser:{}",JSON.toJSONString(weChatMiniUser));

        if(weChatMiniUser==null){
            weChatMiniUser = new WeChatMiniUser();
            weChatMiniUser.setUnionId(unionId);
            weChatMiniUser.setOpenId(openId);
            weChatMiniUserService.saveWeChatMiniUser(weChatMiniUser);
        }
        log.info("登陆时候获取的openId:{}",openId);
        WechatUser wechatUser = wechatUserService.findByUnionId(unionId);
        if(wechatUser==null){
            wechatUser = new WechatUser();
        }
        //wechatUser.setOpenId(openId);
        wechatUser =  wechatUserService.save(wechatUser);
        log.info("wechatUser:{}",JSON.toJSONString(wechatUser));

        String wechatId =wechatUser.getId();
        WechatUserInfo wechatUserInfo = wechatUserInfoService.findByWechatId(wechatId);
        if(wechatUserInfo==null) {
            wechatUserInfo = new WechatUserInfo();
        }
        wechatUserInfoService.update(wechatUserInfo);


        String cookie = BCrypt.hashpw(unionId, BCrypt.gensalt());
        wechatMiniCacheService.setWechatMiniUserCache(cookie,unionId);

        restResultModel.setResult(200);
        Map<String,String> resultMap = new HashMap<String,String>();
        resultMap.put("userCookieKey",cookie);
        restResultModel.setData(resultMap);
        return  restResultModel;
    }
    @RequestMapping(value = "/updateWechatUser", method = RequestMethod.POST)
    public RestResultModel updateWechatUser(HttpServletRequest request){
        String userCookieKey = request.getParameter("userCookieKey");

        log.info("更新用户信息获取用户的userCookieKey:{}",userCookieKey);
        RestResultModel restResultModel = new RestResultModel();
        Object object =  wechatMiniCacheService.getWechatMiniUserCache(userCookieKey);
        String unionId = String.valueOf(object);
        WechatUser wechatUser =  wechatUserService.findByUnionId(unionId);

        log.info("取得的用户信息:{}",JSON.toJSONString(wechatUser));

        String avatarUrl = request.getParameter("avatarUrl");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String gender = request.getParameter("gender");
        String nickName = request.getParameter("nickName");
        String language = request.getParameter("language");
        String province = request.getParameter("province");

        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");

        if(wechatUser ==null){
            wechatUser = new WechatUser();
        }
        if(!StringUtils.isEmpty(avatarUrl)){
            wechatUser.setImgUrl(avatarUrl);
        }
        if(!StringUtils.isEmpty(city)) {
            wechatUser.setCity(city);
        }
        if(!StringUtils.isEmpty(country)) {
            wechatUser.setCountry(country);
        }
        if(!StringUtils.isEmpty(gender)) {
            wechatUser.setSex(IntegerUtils.objectConvertToInt(gender));
        }
        if(!StringUtils.isEmpty(nickName)) {
            wechatUser.setNick(nickName);
        }
        if(!StringUtils.isEmpty(language)) {
            wechatUser.setLanguage(language);
        }
        if(!StringUtils.isEmpty(province)) {
            wechatUser.setProvince(province);
        }

        if(!StringUtils.isEmpty(latitude) && !StringUtils.isEmpty(longitude)) {
            wechatUser.setLatitude(Double.parseDouble(latitude+""));
            wechatUser.setLongitude(Double.parseDouble(longitude+""));
        }



        wechatUser = wechatUserService.save(wechatUser);
        String wechatId = wechatUser.getId();

        WechatUserInfo wechatUserInfo =  wechatUserInfoService.findByWechatId(wechatId);
        if(!StringUtils.isEmpty(latitude) && !StringUtils.isEmpty(longitude)) {
            //wechatUserInfo.setLastLatitude(Double.parseDouble(latitude+""));
            //wechatUserInfo.setLastLongitude(Double.parseDouble(longitude+""));
            wechatUserInfoService.saveOrUpdate(wechatId,Double.parseDouble(longitude+""),Double.parseDouble(latitude+""));
        }
        restResultModel.setResult(200);
        restResultModel.setData(wechatUser);
        return  restResultModel;
    }

    @RequestMapping(value = "/wechartSend", method = RequestMethod.POST)
    public RestResultModel wechartSend(HttpServletRequest request) {
        log.info("小程序端，弹幕发送");
        String userCookieKey = request.getParameter("userCookieKey");
        Object object =  wechatMiniCacheService.getWechatMiniUserCache(userCookieKey);
        String unionId = String.valueOf(object);
        RestResultModel restResultModel = new RestResultModel();
        if (bmsDanmuService.checkFrequency(request)) {
            restResultModel.setResult(403);
            restResultModel.setResult_msg("Limited Frequency");
            log.info("小程序{}，发送弹幕,太频繁",unionId);
            return restResultModel;
        }else if(bmsDanmuService.checkDanmuIsRepeat(unionId,request.getParameter("message"))){
            restResultModel.setResult(403);
            restResultModel.setResult_msg("相同弹幕发送太多");
            log.info("小程序{}，相同弹幕发送太多",unionId);
            return restResultModel;
        }else {
            log.info("===========================================================");
            return bmsDanmuService.sendDanmuFromWechatMini(request,unionId,0);
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
