package cn.partytime.model.client;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by liuwei on 16/6/12.
 */

@Document(collection = "danmu_client_param")
public class DanmuClientParam extends BaseModel {

    @Field("_id")
    private String id;

    private String show;

    private Double expressionScale;

    private Integer maxSize;

    private Integer screen;

    private Double speed1;

    private Double speed2;

    private String bgColor;

    private Boolean bgVisible;

    private Integer offY;

    private Boolean textOrder;

    private Boolean showInDanMu;

    private Boolean localGif;

    private Boolean topShow;

    private Boolean localVideo;

    private Boolean addBgJpg;

    private String bgJpgUrl;

    private Integer setZ;

    private Integer addRectWidth;

    private Integer addRectHeight;

    private Boolean playScreen;

    private Integer valueAbs;

    private Boolean camera;

    private Boolean livePlayer;

    private String liveServer;

    private String liveStream;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public Double getExpressionScale() {
        return expressionScale;
    }

    public void setExpressionScale(Double expressionScale) {
        this.expressionScale = expressionScale;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public Integer getScreen() {
        return screen;
    }

    public void setScreen(Integer screen) {
        this.screen = screen;
    }

    public Double getSpeed1() {
        return speed1;
    }

    public void setSpeed1(Double speed1) {
        this.speed1 = speed1;
    }

    public Double getSpeed2() {
        return speed2;
    }

    public void setSpeed2(Double speed2) {
        this.speed2 = speed2;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public Boolean getBgVisible() {
        return bgVisible;
    }

    public void setBgVisible(Boolean bgVisible) {
        this.bgVisible = bgVisible;
    }

    public Integer getOffY() {
        return offY;
    }

    public void setOffY(Integer offY) {
        this.offY = offY;
    }

    public Boolean getTextOrder() {
        return textOrder;
    }

    public void setTextOrder(Boolean textOrder) {
        this.textOrder = textOrder;
    }

    public Boolean getShowInDanMu() {
        return showInDanMu;
    }

    public void setShowInDanMu(Boolean showInDanMu) {
        this.showInDanMu = showInDanMu;
    }

    public Boolean getLocalGif() {
        return localGif;
    }

    public void setLocalGif(Boolean localGif) {
        this.localGif = localGif;
    }

    public Boolean getTopShow() {
        return topShow;
    }

    public void setTopShow(Boolean topShow) {
        this.topShow = topShow;
    }

    public Boolean getLocalVideo() {
        return localVideo;
    }

    public void setLocalVideo(Boolean localVideo) {
        this.localVideo = localVideo;
    }

    public Boolean getAddBgJpg() {
        return addBgJpg;
    }

    public void setAddBgJpg(Boolean addBgJpg) {
        this.addBgJpg = addBgJpg;
    }

    public String getBgJpgUrl() {
        return bgJpgUrl;
    }

    public void setBgJpgUrl(String bgJpgUrl) {
        this.bgJpgUrl = bgJpgUrl;
    }

    public Integer getSetZ() {
        return setZ;
    }

    public void setSetZ(Integer setZ) {
        this.setZ = setZ;
    }

    public Integer getAddRectWidth() {
        return addRectWidth;
    }

    public void setAddRectWidth(Integer addRectWidth) {
        this.addRectWidth = addRectWidth;
    }

    public Integer getAddRectHeight() {
        return addRectHeight;
    }

    public void setAddRectHeight(Integer addRectHeight) {
        this.addRectHeight = addRectHeight;
    }

    public Boolean getPlayScreen() {
        return playScreen;
    }

    public void setPlayScreen(Boolean playScreen) {
        this.playScreen = playScreen;
    }

    public Integer getValueAbs() {
        return valueAbs;
    }

    public void setValueAbs(Integer valueAbs) {
        this.valueAbs = valueAbs;
    }

    public Boolean getCamera() {
        return camera;
    }

    public void setCamera(Boolean camera) {
        this.camera = camera;
    }

    public Boolean getLivePlayer() {
        return livePlayer;
    }

    public void setLivePlayer(Boolean livePlayer) {
        this.livePlayer = livePlayer;
    }

    public String getLiveServer() {
        return liveServer;
    }

    public void setLiveServer(String liveServer) {
        this.liveServer = liveServer;
    }

    public String getLiveStream() {
        return liveStream;
    }

    public void setLiveStream(String liveStream) {
        this.liveStream = liveStream;
    }
}
