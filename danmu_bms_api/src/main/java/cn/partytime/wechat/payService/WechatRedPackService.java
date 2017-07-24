package cn.partytime.wechat.payService;

import cn.partytime.util.PartyTimeConfig;
import cn.partytime.util.WechatSignUtil;
import cn.partytime.util.WeixinUtil;
import cn.partytime.wechat.entity.ReceiveGroupRedPackXmlEntity;
import cn.partytime.wechat.entity.SendGroupRedPackXmlEntity;
import cn.partytime.wechat.process.FormatXmlProcess;
import cn.partytime.wechat.process.ReceiveXmlProcess;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.*;

/**
 * Created by liuwei on 2016/10/8.
 */
@Slf4j
public class WechatRedPackService {

    private static final String SEND_GROUP_RED_PACK_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendgroupredpack";

    private static final String SEND_RED_PACK_URL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";

    @Autowired
    private PartyTimeConfig partyTimeConfig;

    private CloseableHttpClient  createCertHttpClient() throws Exception{
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File("/Users/administrator/liuwei/wechatpay/apiclient_cert.p12"));
        try {
            keyStore.load(instream, "1368583302".toCharArray());
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, "1368583302".toCharArray())
                .build();

        HostnameVerifier hostnameVerifier =  SSLConnectionSocketFactory.getDefaultHostnameVerifier();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,hostnameVerifier);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

        return httpclient;
    }

    public String postCertHttps(String url,String entityXml) throws Exception{
        CloseableHttpClient  httpclient = createCertHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(entityXml,ContentType.create("text/xml","utf-8")));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        System.out.println("executing request" + httpPost.getRequestLine());

        HttpEntity repsonsEntity = response.getEntity();
        StringBuffer buffer = new StringBuffer();
        try {
            HttpEntity entity = response.getEntity();

            log.debug(url);
            log.debug(""+response.getStatusLine());

            if (entity != null) {
                log.debug("Response content length: " + entity.getContentLength());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                String text;
                while ((text = bufferedReader.readLine()) != null) {
                    buffer.append(text);
                }
                log.info(buffer.toString());
                return buffer.toString();
            }
        } finally {
            response.close();
        }

        return null;
    }

    public String getCertHttps(String url) throws Exception{
        CloseableHttpClient  httpclient = createCertHttpClient();
        StringBuffer buffer = new StringBuffer();
        try{
            HttpGet httpget = new HttpGet(url);
            System.out.println("executing request" + httpget.getRequestLine());

            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();

                log.debug(url);
                log.debug(""+response.getStatusLine());

                if (entity != null) {
                    log.debug("Response content length: " + entity.getContentLength());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String text;
                    while ((text = bufferedReader.readLine()) != null) {
                        buffer.append(text);
                    }
                    log.info(buffer.toString());
                    return buffer.toString();
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return null;
    }


    /**
     * 发送裂变红包
     * @param openId 接收者的openId
     * @param totalAmount 红包的总金额
     * @param totalNum 红包的数量
     * @param wishing  祝福语
     * @param actName  活动名称
     * @param remark  备注信息
     * @return
     * @throws Exception
     */
    public ReceiveGroupRedPackXmlEntity sendGroupRedPack(String openId,Integer totalAmount,Integer totalNum,String wishing,String actName,
                                      String remark  ) throws Exception {
        if( StringUtils.isEmpty(wishing) || StringUtils.isEmpty(actName) || StringUtils.isEmpty(remark) || null == totalNum){
            throw new IllegalArgumentException("祝福语，活动名称,备注信息或者红包数量不能为空");
        }
        if( wishing.length() >128 ){
            throw new IllegalArgumentException("祝福语长度过长");
        }
        if(actName.length()>32){
            throw new IllegalArgumentException("活动名称长度过长");
        }
        if(remark.length()>256){
            throw new IllegalArgumentException("备注信息过长");
        }
        if( totalNum <3 || totalNum >20){
            throw new IllegalArgumentException("红包数量不能小于3个或者大于20个");
        }

        SendGroupRedPackXmlEntity sendGroupRedPackXmlEntity = new SendGroupRedPackXmlEntity();
        sendGroupRedPackXmlEntity.setNonce_str(WechatSignUtil.getRandomString(16));
        sendGroupRedPackXmlEntity.setMch_billno(WeixinUtil.getOrderNo());
        sendGroupRedPackXmlEntity.setMch_id(WeixinUtil.MCH_ID);
        sendGroupRedPackXmlEntity.setWxappid("wx0934dc72c6c20345");
        sendGroupRedPackXmlEntity.setSend_name("聚时代");
        sendGroupRedPackXmlEntity.setRe_openid(openId);
        sendGroupRedPackXmlEntity.setTotal_amount(totalAmount);
        sendGroupRedPackXmlEntity.setTotal_num(totalNum);
        sendGroupRedPackXmlEntity.setAmt_type("ALL_RAND");
        sendGroupRedPackXmlEntity.setWishing(wishing);
        sendGroupRedPackXmlEntity.setAct_name(actName);
        sendGroupRedPackXmlEntity.setRemark(remark);
        String signStr = sendGroupRedPackXmlEntity.toSignString();
        System.out.println(signStr);
        sendGroupRedPackXmlEntity.setSign(WechatSignUtil.createGroupRedPackSign(signStr));

        try {
            String xmlStr = FormatXmlProcess.sendGroupRedPackToXml(sendGroupRedPackXmlEntity);
            xmlStr = xmlStr.replaceAll("__","_");
            System.out.println(xmlStr);
            String repsoneStr = postCertHttps(SEND_GROUP_RED_PACK_URL, xmlStr);
            if(!StringUtils.isEmpty(repsoneStr)){
                ReceiveGroupRedPackXmlEntity receiveGroupRedPackXmlEntity = new ReceiveXmlProcess().getGroupRedPackEntity(repsoneStr);
                return receiveGroupRedPackXmlEntity;
            }
        }catch (Exception e){
            log.error("",e);
        }

        return null;
    }


    /**
     * 发送裂变红包
     * @param openId 接收者的openId
     * @param totalAmount 红包的总金额
     * @param totalNum 红包的数量
     * @param wishing  祝福语
     * @param actName  活动名称
     * @param remark  备注信息
     * @return
     * @throws Exception
     */
    public ReceiveGroupRedPackXmlEntity sendRedPack(String openId,Integer totalAmount,Integer totalNum,String wishing,String actName,
                                                         String remark  ) throws Exception {
        if( StringUtils.isEmpty(wishing) || StringUtils.isEmpty(actName) || StringUtils.isEmpty(remark) || null == totalNum){
            throw new IllegalArgumentException("祝福语，活动名称,备注信息或者红包数量不能为空");
        }
        if( wishing.length() >128 ){
            throw new IllegalArgumentException("祝福语长度过长");
        }
        if(actName.length()>32){
            throw new IllegalArgumentException("活动名称长度过长");
        }
        if(remark.length()>256){
            throw new IllegalArgumentException("备注信息过长");
        }

        SendGroupRedPackXmlEntity sendGroupRedPackXmlEntity = new SendGroupRedPackXmlEntity();
        sendGroupRedPackXmlEntity.setNonce_str(WechatSignUtil.getRandomString(16));
        sendGroupRedPackXmlEntity.setMch_billno(WeixinUtil.getOrderNo());
        sendGroupRedPackXmlEntity.setMch_id(WeixinUtil.MCH_ID);
        sendGroupRedPackXmlEntity.setWxappid("wx0934dc72c6c20345");
        sendGroupRedPackXmlEntity.setSend_name("聚时代");
        sendGroupRedPackXmlEntity.setRe_openid(openId);
        sendGroupRedPackXmlEntity.setTotal_amount(totalAmount);
        sendGroupRedPackXmlEntity.setTotal_num(totalNum);
        sendGroupRedPackXmlEntity.setAmt_type("ALL_RAND");
        sendGroupRedPackXmlEntity.setWishing(wishing);
        sendGroupRedPackXmlEntity.setAct_name(actName);
        sendGroupRedPackXmlEntity.setRemark(remark);
        String signStr = sendGroupRedPackXmlEntity.toSignString();
        System.out.println(signStr);
        sendGroupRedPackXmlEntity.setSign(WechatSignUtil.createGroupRedPackSign(signStr));

        try {
            String xmlStr = FormatXmlProcess.sendGroupRedPackToXml(sendGroupRedPackXmlEntity);
            xmlStr = xmlStr.replaceAll("__","_");
            System.out.println(xmlStr);
            String repsoneStr = postCertHttps(SEND_RED_PACK_URL, xmlStr);
            if(!StringUtils.isEmpty(repsoneStr)){
                ReceiveGroupRedPackXmlEntity receiveGroupRedPackXmlEntity = new ReceiveXmlProcess().getGroupRedPackEntity(repsoneStr);
                return receiveGroupRedPackXmlEntity;
            }
        }catch (Exception e){
            log.error("",e);
        }

        return null;
    }
/**
    public static void main(String[] args) throws Exception {
        WechatRedPackService w = new WechatRedPackService();
        String openId="ol5eSwszFv5dVmv7N_oNp9084JXM";
        Integer totalAmount = 100;
        Integer totalNum = 1;
        String wishing ="这是一个测试";
        String actName = "聚时代的第一个弹幕";
        String remark = "这是一个测试，一共发了1毛钱";
        ReceiveGroupRedPackXmlEntity receiveGroupRedPackXmlEntity = w.sendRedPack(openId,totalAmount,totalNum,wishing,actName,remark);
        if( null != receiveGroupRedPackXmlEntity){
            System.out.println(receiveGroupRedPackXmlEntity.toString());
        }
    }
**/
}
