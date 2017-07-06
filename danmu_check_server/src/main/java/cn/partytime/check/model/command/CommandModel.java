package cn.partytime.check.model.command;

import java.io.Serializable;

/**
 * Created by lENOVO on 2016/11/24.
 */
public class CommandModel implements Serializable {

    private static final long serialVersionUID = -7450885449929762234L;
    /**命令类型*/
    private String type;

    /**秘钥*/
    private String key;

    /**地址编号*/
    private String addressId;

    /**活动编号*/
    private String partyId;

    /**活动名称*/
    private String partyName;

    /**活动命令*/
    private PartyComModel partyCtrl;

    /**预制弹幕*/
    private PreDanmuComModel preDanmu;

    /**屏蔽弹幕命令*/
    private BlockDanmuComModel blockDanmu;

    /**弹幕命令*/
    private DanmuComModel danmu;

    /**视频特效*/
    private VideoComModel video;

    /**表情特效*/
    private ExpComModel expression;

    /**闪光字*/
    private BlingComModel bling;

    /**图片特效*/
    private PictureComModel picture;

    /**屏幕状态*/
    private PlayStatusComModel playStatus;

    /**延迟时间处理*/
    private DelayTimeComModel delayTime;

    /**测试弹幕开启*/
    private TestDanmuComModel testDanmu;

    /**弹幕密度*/
    private DanmuDensityComModel density;

    /**方位*/
    private DanmuDirectionComModel direction;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }


    public PreDanmuComModel getPreDanmu() {
        return preDanmu;
    }

    public void setPreDanmu(PreDanmuComModel preDanmu) {
        this.preDanmu = preDanmu;
    }

    public BlockDanmuComModel getBlockDanmu() {
        return blockDanmu;
    }

    public void setBlockDanmu(BlockDanmuComModel blockDanmu) {
        this.blockDanmu = blockDanmu;
    }

    public DanmuComModel getDanmu() {
        return danmu;
    }

    public void setDanmu(DanmuComModel danmu) {
        this.danmu = danmu;
    }

    public VideoComModel getVideo() {
        return video;
    }

    public void setVideo(VideoComModel video) {
        this.video = video;
    }

    public ExpComModel getExpression() {
        return expression;
    }

    public void setExpression(ExpComModel expression) {
        this.expression = expression;
    }

    public BlingComModel getBling() {
        return bling;
    }

    public void setBling(BlingComModel bling) {
        this.bling = bling;
    }

    public PictureComModel getPicture() {
        return picture;
    }

    public void setPicture(PictureComModel picture) {
        this.picture = picture;
    }

    public PlayStatusComModel getPlayStatus() {
        return playStatus;
    }

    public void setPlayStatus(PlayStatusComModel playStatus) {
        this.playStatus = playStatus;
    }

    public DelayTimeComModel getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(DelayTimeComModel delayTime) {
        this.delayTime = delayTime;
    }

    public TestDanmuComModel getTestDanmu() {
        return testDanmu;
    }

    public void setTestDanmu(TestDanmuComModel testDanmu) {
        this.testDanmu = testDanmu;
    }

    public DanmuDensityComModel getDensity() {
        return density;
    }

    public void setDensity(DanmuDensityComModel density) {
        this.density = density;
    }

    public PartyComModel getPartyCtrl() {
        return partyCtrl;
    }

    public void setPartyCtrl(PartyComModel partyCtrl) {
        this.partyCtrl = partyCtrl;
    }

    public DanmuDirectionComModel getDirection() {
        return direction;
    }

    public void setDirection(DanmuDirectionComModel direction) {
        this.direction = direction;
    }
}
