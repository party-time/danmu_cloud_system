package cn.partytime.model.manager;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

/**
 * Created by liuwei on 16/6/12.
 * 弹幕活动的地址
 */
@Document(collection = "danmu_address")
public class DanmuAddress extends BaseModel {


    private String id;

    //地址别名
    private String name;

    //详细地址
    private String address;

    //所属城市
    private String cityId;

    //所属省份
    private String provinceId;

    //所属区域
    private String areaId;

    //经纬度
    private Location location;

    //场地的长度
    private Integer length;

    //场地的宽度
    private Integer width;

    //场地的高度
    private Integer height;

    //容纳人数
    private Integer peopleNum;

    //场地的联系人
    private String contacts;

    //场地联系人手机号
    private String phoneNum;

    private int range;

    //场地类型 0 固定场地  1 临时场地
    private Integer type;

    //影院商店的状态 0是营业   1是下班
    private Integer shopStatus;

    //该场地下哪些功能可以使用, 功能key:打赏pay  表白love 0或null可以  1不可以
    private Map<String,Integer> controlerStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(Integer shopStatus) {
        this.shopStatus = shopStatus;
    }

    public Map<String, Integer> getControlerStatus() {
        return controlerStatus;
    }

    public void setControlerStatus(Map<String, Integer> controlerStatus) {
        this.controlerStatus = controlerStatus;
    }
}
