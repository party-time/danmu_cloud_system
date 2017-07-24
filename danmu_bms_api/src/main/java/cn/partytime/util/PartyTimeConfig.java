package cn.partytime.util;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Created by liuwei on 2016/10/31.
 */
@ConfigurationProperties(prefix = "partyTime")
public class PartyTimeConfig {

    private String url;

    private String appId;

    private String appSecret;

    private String shopAppId;

    private String shopAddSecret;

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

    public String getShopAppId() {
        return shopAppId;
    }

    public void setShopAppId(String shopAppId) {
        this.shopAppId = shopAppId;
    }

    public String getShopAddSecret() {
        return shopAddSecret;
    }

    public void setShopAddSecret(String shopAddSecret) {
        this.shopAddSecret = shopAddSecret;
    }
}
