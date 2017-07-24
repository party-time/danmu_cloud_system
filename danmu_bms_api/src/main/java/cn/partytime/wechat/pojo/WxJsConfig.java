package cn.partytime.wechat.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/1/24.
 */
public class WxJsConfig implements Serializable{
    private static final long serialVersionUID = 1L;

    public String url;
    public String jsapi_ticket;
    public String nonceStr;
    public String timestamp;
    public String signature;
    private String appId;
    private String packageStr;
    private String paySign;
    private String signType;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJsapi_ticket() {
        return jsapi_ticket;
    }

    public void setJsapi_ticket(String jsapi_ticket) {
        this.jsapi_ticket = jsapi_ticket;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPackageStr() {
        return packageStr;
    }

    public void setPackageStr(String packageStr) {
        this.packageStr = packageStr;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    @Override
    public String toString() {
        return "WxJsConfig{" +
                "url='" + url + '\'' +
                ", jsapi_ticket='" + jsapi_ticket + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", signature='" + signature + '\'' +
                ", appId='" + appId + '\'' +
                ", packageStr='" + packageStr + '\'' +
                ", paySign='" + paySign + '\'' +
                ", signType='" + signType + '\'' +
                '}';
    }

    public String toJsPayString() {
        return "appId="+appId+"&nonceStr=" + nonceStr +
                "&package=" + packageStr +
                "&signType=" + signType +
                "&timeStamp=" + timestamp;
    }


}
