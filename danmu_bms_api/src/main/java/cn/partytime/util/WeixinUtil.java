package cn.partytime.util;

import cn.partytime.model.wechat.MaterialListJson;
import cn.partytime.model.wechat.MaterlListPageResultJson;
import cn.partytime.model.wechat.MaterlResultJson;
import cn.partytime.wechat.entity.ReceiveUnifiedOrderXmlEntity;
import cn.partytime.wechat.pojo.*;
import cn.partytime.wechat.process.FormatXmlProcess;
import cn.partytime.wechat.process.ReceiveXmlProcess;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2014/12/14.
 */
@Slf4j
@Component
public class WeixinUtil {

    @Autowired
    private PartyTimeConfig partyTimeConfig;

    private static WeixinUtil weixinUtil;

    /**
     * 商户号
     */
    public static String MCH_ID = "1368583302";

    // 获取access_token的接口地址（GET） 限200（次/天）
    public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    public final static String userInfo_url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    // 菜单创建（POST） 限100（次/天）
    public final static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    public final static String menu_del_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

    public final static String openid_code_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=APPSECRET&code=CODE&grant_type=authorization_code";
    //js ticket
    public final static String js_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    public final static String UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public final static String send_tmpl_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    public final static String get_material_url ="https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN";

    @PostConstruct
    public void init() {
        weixinUtil = this;
        weixinUtil.partyTimeConfig = this.partyTimeConfig;
    }

    public static AccessToken getAccessToken(){
        AccessToken accessToken = null;
        String requestUrl = access_token_url.replace("APPID", weixinUtil.partyTimeConfig.getAppId()).replace("APPSECRET",weixinUtil.partyTimeConfig.getAppSecret());
        log.info("get accessToken url:"+requestUrl);
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (null != jsonObject) {
            try {
                accessToken = new AccessToken();
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
            } catch (JSONException e) {
                accessToken = null;
                // 获取token失败
                log.error("获取token失败 errcode:{"+ jsonObject.getInt("errcode")+"} errmsg:{"+ jsonObject.getString("errmsg")+"}");
            }
        }
        return accessToken;
    }

    /**
     * 获取用户信息
     *
     * @param access_token 凭证
     * @param openId 用户id
     * @return
     */
    public static UserInfo getUserInfo(String access_token, String openId) {
        UserInfo userInfo = null;

        String requestUrl = userInfo_url.replace("ACCESS_TOKEN", access_token).replace("OPENID", openId);
        log.info("getUserInfo url:"+requestUrl);

        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (null != jsonObject) {
            try {
                userInfo = new UserInfo();
                if(jsonObject.has("subscribe")){
                    userInfo.setSubscribe(jsonObject.getInt("subscribe"));
                }
                if(jsonObject.has("openid")) {
                    userInfo.setOpenid(jsonObject.getString("openid"));
                }
                if(jsonObject.has("nickname")){
                    userInfo.setNickname(EmojiFilter.filterEmoji(jsonObject.getString("nickname")));
                }
                if(jsonObject.has("sex")){
                    userInfo.setSex(jsonObject.getInt("sex"));
                }
                if(jsonObject.has("language")){
                    userInfo.setLanguage(jsonObject.getString("language"));
                }
                if(jsonObject.has("city")){
                    userInfo.setCity(jsonObject.getString("city"));
                }
                if(jsonObject.has("province")){
                    userInfo.setProvince(jsonObject.getString("province"));
                }
                if(jsonObject.has("country")){
                    userInfo.setCountry(jsonObject.getString("country"));
                }
                if(jsonObject.has("headimgurl")){
                    userInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
                }
                if(jsonObject.has("subscribe_time")){
                    userInfo.setSubscribe_time(jsonObject.getLong("subscribe_time"));
                }

                //unionId有问题
                if(jsonObject.has("openid")) {
                    userInfo.setUnionid(jsonObject.getString("openid"));
                }
            } catch (JSONException e) {
                userInfo = null;
                // 获取token失败
                log.error("getUserInfo errer:",e);
                log.error("获取userInfo失败 errcode:{"+ jsonObject.get("errcode")+"}");
            }
        }
        return userInfo;
    }
    /**
     * 根据code获取用户OPENID
     *
     * @param appid
     * @param appsecret
     * @param code 用户code
     * @return
     */
    public static String getUserOpenId(String appid, String appsecret, String code) {
        String requestUrl = openid_code_url.replace("APPID", appid).replace("APPSECRET", appsecret).replace("CODE", code);
        log.info("code=>openid url:"+requestUrl);

        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        String openId="";
        // 如果请求成功
        if (null != jsonObject) {
            try {
                openId = jsonObject.getString("openid");
            } catch (JSONException e) {
                // 获取token失败
                log.error("获取openId失败 errcode:{"+ jsonObject.getInt("errcode")+"} errmsg:{"+ jsonObject.getString("errmsg")+"}");
            }
        }
        log.info("code=>openid:"+openId);
        return openId;
    }

    /**
     * 根据code获取用户OPENID
     *
     * @param code 用户code
     * @return
     */
    public static String getUserOpenId( String code) {
        if(StringUtils.isEmpty(code)){
            return null;
        }
        String requestUrl = openid_code_url.replace("APPID",weixinUtil.partyTimeConfig.getAppId() ).replace("APPSECRET", weixinUtil.partyTimeConfig.getAppSecret()).replace("CODE", code);
        log.info("code=>openid url:"+requestUrl);

        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        String openId="";
        // 如果请求成功
        if (null != jsonObject) {
            try {
                openId = jsonObject.getString("openid");
            } catch (JSONException e) {
                // 获取token失败
                log.error("获取openId失败 errcode:{"+ jsonObject.getInt("errcode")+"} errmsg:{"+ jsonObject.getString("errmsg")+"}");
            }
        }
        log.info("code=>openid:"+openId);
        return openId;
    }

    /**
     * 创建菜单
     *
     * @param menu 菜单实例
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public static int createMenu(Menu menu, String accessToken) {
        int result = 0;

        // 拼装创建菜单的url
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);

        // 将菜单对象转换成json字符串
        String jsonMenu = JSONObject.fromObject(menu).toString();
        // 调用接口创建菜单
        JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);
        log.info(jsonMenu);
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                log.error("创建菜单失败 errcode:{"+ jsonObject.getInt("errcode")+"} errmsg:{"+ jsonObject.getString("errmsg")+"}");
            }
        }
        return result;
    }

    /**
     * 创建菜单
     *
     * @param menuJson 菜单的json串
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public static int createMenu(String menuJson, String accessToken) {
        int result = 0;
        // 拼装创建菜单的url
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);

        // 调用接口创建菜单
        JSONObject jsonObject = httpRequest(url, "POST", menuJson);
        log.info(menuJson);
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                log.error("创建菜单失败 errcode:{"+ jsonObject.getInt("errcode")+"} errmsg:{"+ jsonObject.getString("errmsg")+"}");
            }
        }
        return result;
    }
    /**
     * 删除菜单
     *
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public static int delMenu(String accessToken) {
        int result = 0;

        // 拼装创建菜单的url
        String url = menu_del_url.replace("ACCESS_TOKEN", accessToken);

        // 调用接口创建菜单
        JSONObject jsonObject = httpRequest(url, "GET", null);

        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                log.error("创建菜单失败 errcode:{"+ jsonObject.getInt("errcode")+"} errmsg:{"+ jsonObject.getString("errmsg")+"}");
            }
        }
        return result;
    }


    /**
     * 获取jsticket
     *
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public static String getJsTicket(String accessToken) {
        String result = null;

        // 拼装创建菜单的url
        String url = js_ticket_url.replace("ACCESS_TOKEN", accessToken);

        // 调用接口创建菜单
        JSONObject jsonObject = httpRequest(url, "GET", null);

        // 如果请求成功
        if (null != jsonObject) {
            try {
                result=jsonObject.getString("ticket");
            } catch (JSONException e) {
                // 获取token失败
                log.error("ticket errcode:{"+ jsonObject.getInt("errcode")+"} errmsg:{"+ jsonObject.getString("errmsg")+"}");
            }
        }
        return result;
    }

    /**
     * 获取jsticket
     *
     * @return 0表示成功，其他值表示失败
     */
    public static String getJsTicket(AccessToken accessToken) {
        String result = null;
        // 拼装创建菜单的url
        String url = js_ticket_url.replace("ACCESS_TOKEN", accessToken.getToken());

        // 调用接口创建菜单
        JSONObject jsonObject = httpRequest(url, "GET", null);

        // 如果请求成功
        if (null != jsonObject) {
            try {
                result=jsonObject.getString("ticket");
            } catch (JSONException e) {
                // 获取token失败
                log.error("ticket errcode:{"+ jsonObject.getInt("errcode")+"} errmsg:{"+ jsonObject.getString("errmsg")+"}");
            }
        }
        return result;
    }

    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
            log.info("getInfo from wechat :"+jsonObject.toString());
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        return jsonObject;
    }

    public static String httpRequestStr(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = new StringBuffer();
        outputStr = outputStr.replaceAll("__","_");
        log.info("unifiedorderXML="+outputStr);
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            httpUrlConn.setRequestProperty("Content-Type ","text/xml;charset=UTF-8");
            httpUrlConn.setRequestProperty("Content-Length", String.valueOf(outputStr.length()));
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            //if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
            }
            Integer code = httpUrlConn.getResponseCode();
            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            log.info("getInfo from wechat :"+buffer.toString());
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        return buffer.toString();
    }
//    public static void main(String args[]){
//
////        https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx94b5ae58255c0419&secret=e79241b257f29124584cae2a3f4dbd71&code=001d58e2a05c2b1d75ccc1101f4df2aV&grant_type=authorization_code
//
//
//        getUserInfo("24yRO8LzAmJmQ-8oo9OLa4agH5shpCYjLUxZIdv9TO_nH_Kd7-aUMMHfxcyEirdJFCey6Y7uULEYn7k7IfG7qr_z6mcaZNaRHyordrIrVNg","o95pduOSYE7GA6xRl58Znc-fJWeo");
//    }

    /**
     * 统一下单接口
     */
    public static ReceiveUnifiedOrderXmlEntity unifiedorder(String body,String detail,String attach,Integer totalFee,String spbillCreateIp,String openId){
        UnifiedorderRequest unifiedorderRequest = new UnifiedorderRequest();
        unifiedorderRequest.setAppid(weixinUtil.partyTimeConfig.getAppId());
        unifiedorderRequest.setMch_id(MCH_ID);
        unifiedorderRequest.setDevice_info("WEB");
        unifiedorderRequest.setNonce_str(WechatSignUtil.getRandomString(16));
        unifiedorderRequest.setBody(body);
        unifiedorderRequest.setDetail(detail);
        unifiedorderRequest.setAttach(attach);
        unifiedorderRequest.setOut_trade_no(getOrderNo());
        unifiedorderRequest.setTotal_fee(totalFee);
        unifiedorderRequest.setSpbill_create_ip(spbillCreateIp);
        unifiedorderRequest.setNotify_url(weixinUtil.partyTimeConfig.getUrl()+"/wechat/payNotify");
        unifiedorderRequest.setTrade_type("JSAPI");
        unifiedorderRequest.setOpenid(openId);
        log.info(unifiedorderRequest.toSignString());
        String sign = WechatSignUtil.createPaySign(unifiedorderRequest.toSignString());
        unifiedorderRequest.setSign(sign);
        String result = httpRequestStr(UNIFIEDORDER_URL, "POST", FormatXmlProcess.unifiedorderRequestToXml(unifiedorderRequest));
        ReceiveUnifiedOrderXmlEntity entity = new ReceiveXmlProcess().getUnifiedOrderEntity(result);
        log.info("***unifiedorder="+result);
        return entity;
    }

    /**
     * 统一下单接口
     */
    public static ReceiveUnifiedOrderXmlEntity unifiedorder(String nonceStr,String body,String detail,String attach,Integer totalFee,String spbillCreateIp,String openId){
        UnifiedorderRequest unifiedorderRequest = new UnifiedorderRequest();
        unifiedorderRequest.setAppid(weixinUtil.partyTimeConfig.getAppId());
        unifiedorderRequest.setMch_id(MCH_ID);
        unifiedorderRequest.setDevice_info("WEB");
        unifiedorderRequest.setNonce_str(nonceStr);
        unifiedorderRequest.setBody(body);
        unifiedorderRequest.setDetail(detail);
        unifiedorderRequest.setAttach(attach);
        unifiedorderRequest.setOut_trade_no(getOrderNo());
        unifiedorderRequest.setTotal_fee(totalFee);
        unifiedorderRequest.setSpbill_create_ip(spbillCreateIp);
        unifiedorderRequest.setNotify_url(weixinUtil.partyTimeConfig.getUrl()+"/wechat/payNotify");
        unifiedorderRequest.setTrade_type("JSAPI");
        unifiedorderRequest.setOpenid(openId);
        log.info(unifiedorderRequest.toSignString());
        String sign = WechatSignUtil.createPaySign(unifiedorderRequest.toSignString());
        unifiedorderRequest.setSign(sign);
        String result = httpRequestStr(UNIFIEDORDER_URL, "POST", FormatXmlProcess.unifiedorderRequestToXml(unifiedorderRequest));
        ReceiveUnifiedOrderXmlEntity entity = new ReceiveXmlProcess().getUnifiedOrderEntity(result);
        log.info("***unifiedorder="+result);
        return entity;
    }

    /**
     * 生成订单编号
     * @return
     */
    public static synchronized String getOrderNo() {
        String str = new SimpleDateFormat("yyyyMMdd").format(new Date());
        long orderNo = Long.parseLong((str));
        Long ll = new Date().getTime();
        return MCH_ID+orderNo+Math.abs(ll.intValue());
    }


    /**
     * 发送模版消息
     * @param tmplJson
     * @param accessToken
     */
    public static JSONObject sendTmpl(String tmplJson, String accessToken){

        String url = send_tmpl_url.replace("ACCESS_TOKEN", accessToken);

        // 调用接口创建菜单
        return httpRequest(url, "POST", tmplJson);


    }


    public static MaterlListPageResultJson getMaterial(String accessToken, MaterialListJson materialListJson){
        String url = get_material_url.replace("ACCESS_TOKEN",accessToken);

        JSONObject jsonObject = httpRequest(url, "POST", JSONObject.fromObject(materialListJson).toString());

        log.info(jsonObject.toString());
        Map<String, Class> classMap = new HashMap<String, Class>();
        classMap.put("item", MaterlResultJson.class);
        Object obj = JSONObject.toBean(jsonObject,MaterlListPageResultJson.class,classMap);
        return (MaterlListPageResultJson)obj;

    }

}
