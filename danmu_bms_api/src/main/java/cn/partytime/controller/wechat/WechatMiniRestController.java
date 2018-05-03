package cn.partytime.controller.wechat;

import cn.partytime.dataRpc.RpcCmdService;
import cn.partytime.model.*;
import cn.partytime.model.manager.FastDanmu;
import cn.partytime.model.manager.H5Template;
import cn.partytime.model.manager.ResourceFile;
import cn.partytime.model.wechat.UseSecretInfo;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.model.wechat.WechatUserInfo;
import cn.partytime.service.*;
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
    private WechatUserInfoService wechatUserInfoService;

    @Autowired
    private WechatUserService wechatUserService;

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

        String code  = request.getParameter("code");
        log.info("小程序请求的code:{}",code);
        UseSecretInfo useSecretInfo = WeixinUtil.getMiniProgramUserOpenIdAndSessionKey(code);
        log.info("useSecretInfo:{}",JSON.toJSONString(useSecretInfo));

        RestResultModel restResultModel = new RestResultModel();
        String openId = "oze02wVALzhbkpW9f7r3g036O6vw";
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
            log.info("===========================================================");
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

    @RequestMapping(value = "/historyDanmu/{openId}/{pageNo}/{pageSize}", method = RequestMethod.GET)
    public PageResultModel historyDanmu(@PathVariable("openId")String openId , @PathVariable("pageNo") Integer pageNo, @PathVariable("pageSize")Integer pageSize) {
        PartyLogicModel party = bmsWechatUserService.findPartyByOpenId(openId);
        PageResultModel pageResultModel = bmsDanmuService.findPageResultModel(pageNo,pageSize,party.getAddressId(),party.getPartyId(),1);
        return pageResultModel;
    }
}
