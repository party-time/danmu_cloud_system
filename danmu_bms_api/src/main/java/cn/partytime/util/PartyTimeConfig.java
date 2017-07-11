package cn.partytime.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by liuwei on 2016/10/31.
 */
@Component
public class PartyTimeConfig {

    @Value("${partyTime.url}")
    private String url;

    @Value("${partyTime.appId}")
    private String appId;

    @Value("${partyTime.appSecret}")
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
