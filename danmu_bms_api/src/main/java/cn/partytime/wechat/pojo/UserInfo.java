package cn.partytime.wechat.pojo;

import cn.partytime.model.wechat.WechatUser;
import org.springframework.util.StringUtils;

/**
 * 接口访问凭证
 * Created by on 2014/12/14.
 */
public class UserInfo {

    // 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
    private int subscribe;
    // 用户的标识，对当前公众号唯一
    private String openid;
    //用户的昵称
    private String nickname;
    //用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    private int sex;
    //用户所在城市
    private String city	;
    //用户所在国家
    private String country;
    //用户所在省份
    private String province;
    //用户的语言，简体中文为zh_CN
    private String language;
    //用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
    private String headimgurl;
    //用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
    private long subscribe_time;
    //只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。
    private String unionid;


    public WechatUser toWechatUser(){
        WechatUser wechatUser = new WechatUser();
        if(!StringUtils.isEmpty(this.getCity())){
            wechatUser.setCity(this.getCity());
        }
        if(!StringUtils.isEmpty(this.getCountry())){
            wechatUser.setCountry(this.getCountry());
        }
        if(!StringUtils.isEmpty(this.getHeadimgurl())){
            wechatUser.setImgUrl(this.getHeadimgurl());
        }
        if(!StringUtils.isEmpty(this.getLanguage())){
            wechatUser.setLanguage(this.getLanguage());
        }
        if(!StringUtils.isEmpty(this.getNickname())){
            wechatUser.setNick(this.getNickname());
        }
        if(StringUtils.isEmpty(this.getSex())){
            wechatUser.setSex(this.getSex());
        }
        if(StringUtils.isEmpty(this.getProvince())){
            wechatUser.setProvince(this.getProvince());
        }
        if(StringUtils.isEmpty(this.getOpenid())){
            wechatUser.setOpenId(this.getOpenid());
        }
        if(StringUtils.isEmpty(this.getSubscribe_time())){
            wechatUser.setSubscribeTime(this.getSubscribe_time());
        }
        if(StringUtils.isEmpty(this.getUnionid())){
            wechatUser.setUnionId(this.getUnionid());
        }

        return wechatUser;
    }

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public long getSubscribe_time() {
        return subscribe_time;
    }

    public void setSubscribe_time(long subscribe_time) {
        this.subscribe_time = subscribe_time;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "subscribe=" + subscribe +
                ", openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", language='" + language + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", subscribe_time=" + subscribe_time +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
