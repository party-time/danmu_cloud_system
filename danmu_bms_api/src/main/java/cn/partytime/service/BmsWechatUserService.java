package cn.partytime.service;


import cn.partytime.common.cachekey.LoginCodeCacheKey;
import cn.partytime.common.cachekey.wechat.WechatCacheKey;
import cn.partytime.model.PartyLogicModel;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.Party;
import cn.partytime.model.wechat.MaterialListJson;
import cn.partytime.model.wechat.MaterlListPageResultJson;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.model.wechat.WechatUserInfo;
import cn.partytime.model.wechatMsgTmpl.MsgTmpl;
import cn.partytime.redis.service.RedisService;
import cn.partytime.service.wechat.WechatUserInfoService;
import cn.partytime.service.wechat.WechatUserLogService;
import cn.partytime.service.wechat.WechatUserService;
import cn.partytime.util.MessageUtil;
import cn.partytime.util.PartyTimeConfig;
import cn.partytime.util.WeixinUtil;
import cn.partytime.wechat.entity.ReceiveXmlEntity;
import cn.partytime.wechat.message.TextMessage;
import cn.partytime.wechat.pojo.AccessToken;
import cn.partytime.wechat.pojo.UserInfo;
import cn.partytime.wechat.process.FormatXmlProcess;
import com.alibaba.fastjson.JSON;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.*;

/**
 * Created by liuwei on 16/7/21.
 */
@Service
@Slf4j
public class BmsWechatUserService {

    private static final Logger logger = LoggerFactory.getLogger(BmsWechatUserService.class);

    @Autowired
    private Configuration configuration;

    @Autowired
    private BmsPartyService bmsPartyService;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PartyTimeConfig partyTimeConfig;

    @Autowired
    private WechatUserLogService wechatUserLogService;

    public static final String numberLowerLetterChar = "0123456789";

    @Autowired
    private WechatUserInfoService wechatUserInfoService;

    @Autowired
    private DanmuAddressService danmuAddressService;

    @Value("${loginTemplate.id}")
    private String templateId;


    /**
     * 获取h5 绝对地址
     * @param openId
     * @return
     */
    public String findH5Url(String openId){
        PartyLogicModel party =  bmsPartyService.findCurrentParty(openId);
        if(party==null){
            return StringUtils.EMPTY;
        }
        String pageName = party.getShortName();
        return partyTimeConfig.getUrl()+"/wechat/sendDM/"+pageName;
    }

    public PartyLogicModel findPartyByOpenId(String openId){
        logger.info("用户请求的openId:{}",openId);
        return  bmsPartyService.findCurrentParty(openId);
    }

    public PartyLogicModel findPartyByAddressId(){

        return bmsPartyService.findCurrentParty(Double.parseDouble("39.9903267"),Double.parseDouble("116.4926692421"));
    }

    public WechatUser subscribe(UserInfo userInfo){
        if( null != userInfo) {
            WechatUser wechatUserModel = userInfo.toWechatUser();
            wechatUserModel.setSubscribeState(0);
            WechatUser wechatUser = wechatUserService.save(wechatUserModel);
            wechatUserInfoService.saveOrUpdate(wechatUser.getId(),null,null);
            wechatUserLogService.save(wechatUserModel.getOpenId(),"subscribe");
            return wechatUser;
        }else{
            logger.info("没有获取到微信用户的信息");
            return null;
        }
    }


    public WechatUser saveUserLocation(WechatUser wechatUser,ReceiveXmlEntity xmlEntity){
        //当管理员给用户分配场地时，在30分钟内不修改用户场地
        if( null != wechatUser.getAssignAddressTime()){
            Date now  = new Date();
            long aa = now.getTime() - wechatUser.getAssignAddressTime().getTime();
            if( (aa /(1000*60)) > 30 ){
                wechatUserInfoService.saveOrUpdate(wechatUser.getId(),Double.parseDouble(xmlEntity.getLongitude()),Double.parseDouble(xmlEntity.getLatitude()));
                wechatUserLogService.save(wechatUser.getOpenId(),"{getLocation:{longitude:"+xmlEntity.getLongitude()+",latitude:"+xmlEntity.getLatitude()+"}}");
            }
        }else{
            wechatUserInfoService.saveOrUpdate(wechatUser.getId(),Double.parseDouble(xmlEntity.getLongitude()),Double.parseDouble(xmlEntity.getLatitude()));
            wechatUserLogService.save(wechatUser.getOpenId(),"{getLocation:{longitude:"+xmlEntity.getLongitude()+",latitude:"+xmlEntity.getLatitude()+"}}");
        }
        return wechatUser;
    }


    public WechatUser updateUserLocation(String openId,String longitude,String latitude){
        WechatUser wechatUser = wechatUserService.findByOpenId(openId);
        if( null != wechatUser){
            wechatUserInfoService.saveOrUpdate(wechatUser.getId(),Double.parseDouble(longitude),Double.parseDouble(latitude));
        }
        return wechatUser;
    }

    public AccessToken getAccessToken(){
        Object object =redisService.get(WechatCacheKey.WECHAT_CACHE_KEY);
        AccessToken accessToken = null;
        if( null !=  object) {
            accessToken = JSON.parseObject((String)object,AccessToken.class);
        }else{
            accessToken = new WeixinUtil().getAccessToken();
            redisService.set(WechatCacheKey.WECHAT_CACHE_KEY,  JSON.toJSONString(accessToken), 3600);
        }
       return accessToken;
    }

    public AccessToken getAccessToken1(){
        Object object =redisService.get(WechatCacheKey.WECHAT_CACHE_KEY_1);
        AccessToken accessToken = null;
        if( null !=  object) {
            accessToken = JSON.parseObject((String)object,AccessToken.class);
        }else{
            accessToken = new WeixinUtil().getAccessToken1();
            redisService.set(WechatCacheKey.WECHAT_CACHE_KEY_1,  JSON.toJSONString(accessToken), 3600);
        }
        return accessToken;
    }


    public String getJsTicket(String accessToken){
        Object object =redisService.get(WechatCacheKey.WECHAT_JS_TICKET_CACHE_KEY);
        String jsTicket = null;
        if( null != object){
            jsTicket = (String)object;
        }else{
            jsTicket = WeixinUtil.getJsTicket(accessToken);
            redisService.set(WechatCacheKey.WECHAT_JS_TICKET_CACHE_KEY, jsTicket, 3600);
        }
        return jsTicket;
    }


    public void createMenu( ){
        AccessToken accessToken = this.getAccessToken();
        Template template = null; // freeMarker template
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("appId",partyTimeConfig.getAppId());
        model.put("url",partyTimeConfig.getUrl());
        String content = null;
        try {
            template = configuration.getTemplate("wechat/menu.ftl");
            content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        WeixinUtil.createMenu(content, accessToken.getToken());
    }

    public void delMenu(){
        AccessToken accessToken = this.getAccessToken();
        // 调用接口创建菜单
        int result = WeixinUtil.delMenu(accessToken.getToken());
        // 判断菜单创建结果
        if (0 == result)
            log.info("菜单删除成功！");
        else
            log.error("菜单删除失败，错误码：" + result);
    }



    public String sendVoiceDanmu(ReceiveXmlEntity xmlEntity){
        TextMessage text = new TextMessage();
        text.setToUserName(xmlEntity.getFromUserName());
        text.setFromUserName(xmlEntity.getToUserName());
        text.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        text.setCreateTime(new Date().getTime());
        text.setFuncFlag(0);
        WechatUser wechatUser = this.findByOpenId(xmlEntity.getFromUserName());
        if( null != wechatUser  ){
            WechatUserInfo wechatUserInfo = wechatUserInfoService.findByWechatId(wechatUser.getId());
            if( null == wechatUserInfo || null == wechatUserInfo.getLastLongitude() || null == wechatUserInfo.getLastLatitude()){
                text.setContent("老司机定位失败，请打开“提供位置信息”或者请点击<a href='https://open.weixin.qq.com/connect/oauth2/authorize?appid="+partyTimeConfig.getAppId()+"&redirect_uri="+partyTimeConfig.getUrl()+"/wechat/sendDM&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect'>这里提供您的位置信息</a>");
                return FormatXmlProcess.textMessageToXml(text);
            }else{
                if( null ==  wechatUserInfo.getLastGetLocationDate()){
                    text.setContent("老司机定位失败，请打开“提供位置信息”或者请点击<a href='https://open.weixin.qq.com/connect/oauth2/authorize?appid="+partyTimeConfig.getAppId()+"&redirect_uri="+partyTimeConfig.getUrl()+"/wechat/sendDM&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect'>这里提供您的位置信息</a>");
                    return FormatXmlProcess.textMessageToXml(text);
                }
                long diff = new Date().getTime() -wechatUserInfo.getLastGetLocationDate().getTime();
                long hour = diff/(1000 * 60 * 60);
                if( hour > 24){
                    text.setContent("老司机定位失败，请打开“提供位置信息”或者请点击<a href='https://open.weixin.qq.com/connect/oauth2/authorize?appid="+partyTimeConfig.getAppId()+"&redirect_uri="+partyTimeConfig.getUrl()+"/wechat/sendDM&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect'>这里提供您的位置信息</a>");
                    return FormatXmlProcess.textMessageToXml(text);
                }
            }
        }

        String h5Url = this.findH5Url(xmlEntity.getFromUserName());
        if(org.springframework.util.StringUtils.isEmpty(h5Url)){
            text.setContent("对不起,电影尚未开始。请稍等片刻！");
        }else{
            text.setContent("1.恭喜您成功解锁语音弹（tu）幕（cao）观影模式！对着公众账号说话，就可以发送弹幕！");
        }
        return FormatXmlProcess.textMessageToXml(text);
    }

    public String sendDanmu(ReceiveXmlEntity xmlEntity){

        TextMessage text = new TextMessage();
        text.setToUserName(xmlEntity.getFromUserName());
        text.setFromUserName(xmlEntity.getToUserName());
        text.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        text.setCreateTime(new Date().getTime());
        text.setFuncFlag(0);
        String h5Url = this.findH5Url(xmlEntity.getFromUserName());
        if(org.springframework.util.StringUtils.isEmpty(h5Url)){
            text.setContent("对不起,电影尚未开始。请稍等片刻！");
        }else{
            text.setContent("1.恭喜您成功解锁语音弹（tu）幕（cao）观影模式！对着公众账号说话，就可以发送弹幕！");
        }
        return FormatXmlProcess.textMessageToXml(text);
    }

    public WechatUser findByOpenId(String openId){
        return wechatUserService.findByOpenId(openId);
    }


    private String getCode(){

        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for(int i = 0; i < 6; i++) {
            sb.append( numberLowerLetterChar.charAt( random.nextInt( numberLowerLetterChar.length() ) ) );
        }
        return sb.toString();
    }



    public String sendLoginTmpl(String openId){

        Object object = redisService.get(LoginCodeCacheKey.LOGIN_TIME_KEY+openId);
        if( null != object){
            Date lastGetCodeTime = (Date)object;
            long interval = (new Date().getTime()-lastGetCodeTime.getTime())/1000;
            if(interval<120 ){
                return "获取验证码过于频繁，请稍后在尝试";
            }else{
                //记录获取验证码的时间
                redisService.set(LoginCodeCacheKey.LOGIN_TIME_KEY+openId, new Date(),300);
            }
        }else{
            //记录获取验证码的时间
            redisService.set(LoginCodeCacheKey.LOGIN_TIME_KEY+openId, new Date(),300);
        }
        log.info("get accesstoken start");
        AccessToken accessToken = this.getAccessToken();
        log.info("get accesstoken end");
        Template template = null; // freeMarker template
        String code = getCode();
        redisService.set(LoginCodeCacheKey.LOGIN_CODE_CACHE_KEY+openId, code, 300);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("openId",openId);
        model.put("code",code);
        model.put("sendDate",new Date());

        //production :WHjnHt4lMaykggCTIrFRZCr-aF-ZnE3LHyrpjoH0H_g
        //test:CMwGIS1kJkWxMrkYoNMi2W9D5ywaOibG-rXwvaitsPY
        model.put("templateId",templateId);
        String content = null;
        try {
            template = configuration.getTemplate("wechat/tmpl/loginTmplJson.ftl");
            content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        log.info("send tmpl start");
        JSONObject jsonObject = WeixinUtil.sendTmpl(content, accessToken.getToken());
        if( null != jsonObject){
            if(jsonObject.getInt("errcode")==40001){
                redisService.deleteByKey(WechatCacheKey.WECHAT_CACHE_KEY);
            }
        }
        log.info("send tmpl end");
        return null;
    }

    public String sendTeamViewTmpl(String openId,String addressName,String registCode){
        AccessToken accessToken = this.getAccessToken();
        Template template = null; // freeMarker template
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("openId",openId);
        model.put("addressName",addressName);
        model.put("registCode",registCode);
        model.put("sendDate",new Date());
        model.put("url",partyTimeConfig.getUrl()+"/htm/screen.html?registCode="+registCode);
        String content = null;
        try {
            template = configuration.getTemplate("wechat/tmpl/teamViewTmplJson.ftl");
            content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        WeixinUtil.sendTmpl(content, accessToken.getToken());
        return null;
    }

    public void sendMsg(MsgTmpl msgTmpl){
        AccessToken accessToken = this.getAccessToken();
        JSONObject jsonObject = WeixinUtil.sendTmpl(JSONObject.fromObject(msgTmpl).toString(), accessToken.getToken());
    }

    public MaterlListPageResultJson getVoice(int page, int size){
        AccessToken accessToken = this.getAccessToken();
        MaterialListJson materialListJson = new MaterialListJson();
        materialListJson.setType("voice");
        int offSet = (page-1)*size;
        materialListJson.setOffset(offSet);
        materialListJson.setCount(size);
        return WeixinUtil.getMaterial(accessToken.getToken(),materialListJson);
    }

    public MaterlListPageResultJson getImage(int page, int size){
        AccessToken accessToken = this.getAccessToken();
        MaterialListJson materialListJson = new MaterialListJson();
        materialListJson.setType("image");
        int offSet = (page-1)*size;
        materialListJson.setOffset(offSet);
        materialListJson.setCount(size);
        return WeixinUtil.getMaterial(accessToken.getToken(),materialListJson);
    }
}
