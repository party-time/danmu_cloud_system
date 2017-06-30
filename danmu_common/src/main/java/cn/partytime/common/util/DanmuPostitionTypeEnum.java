package cn.partytime.common.util;

/**
 * Created by lENOVO on 2016/11/22.
 */
public enum DanmuPostitionTypeEnum {
    POSITION_FULL(0, "全部", "FULL"),
    POSITION_TL(1, "左上", "TL"),
    POSITION_TC(2, "顶部", "TC"),
    POSITION_TR(3, "右上", "TR"),
    POSITION_BL(4, "左下", "BL"),
    POSITION_BC(5, "底部", "BC"),
    POSITION_BR(6, "右下", "BR");
    private String cnName;
    private String enName;
    private int index;

    DanmuPostitionTypeEnum(int index, String cnName, String enName) {
        this.cnName = cnName;
        this.enName = enName;
        this.index = index;
    }

    public static String getCnName(int index) {
        for (DanmuPostitionTypeEnum danmuPostitionTypeEnum : DanmuPostitionTypeEnum.values()) {
            if (danmuPostitionTypeEnum.getIndex() == index) {
                return danmuPostitionTypeEnum.getCnName();
            }
        }
        return null;
    }

    public static String getEnName(int index) {
        for (DanmuPostitionTypeEnum danmuPostitionTypeEnum : DanmuPostitionTypeEnum.values()) {
            if (danmuPostitionTypeEnum.getIndex() == index) {
                return danmuPostitionTypeEnum.getEnName();
            }
        }
        return null;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
