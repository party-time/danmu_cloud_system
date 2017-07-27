package cn.partytime.model.shop;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Created by administrator on 2017/6/26.
 */
public class Item {


    private String id;

    //商品名称
    private String name;

    //商品标题
    private String title;

    //商品实际价格
    private Integer truePrice;

    //商品展示价格
    private Integer showPrice;

    //商品封面图片
    private String coverImgId;

    //商品图片列表
    private List<String> imageIds;

    //商品内容
    private String content;

    //商品分类
    private Integer type;

    //跳转的页面
    private String url;

    //关联的弹幕指令的id
    private String dmCmdId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTruePrice() {
        return truePrice;
    }

    public void setTruePrice(Integer truePrice) {
        this.truePrice = truePrice;
    }

    public Integer getShowPrice() {
        return showPrice;
    }

    public void setShowPrice(Integer showPrice) {
        this.showPrice = showPrice;
    }

    public String getCoverImgId() {
        return coverImgId;
    }

    public void setCoverImgId(String coverImgId) {
        this.coverImgId = coverImgId;
    }

    public List<String> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<String> imageIds) {
        this.imageIds = imageIds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDmCmdId() {
        return dmCmdId;
    }

    public void setDmCmdId(String dmCmdId) {
        this.dmCmdId = dmCmdId;
    }
}
