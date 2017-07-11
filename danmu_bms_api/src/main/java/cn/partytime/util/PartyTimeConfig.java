package cn.partytime.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by liuwei on 2016/10/31.
 */
@ConfigurationProperties(prefix = "partyTime")
public class PartyTimeConfig {

    private String url;

    private String appId;

    private String appSecret;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
}
