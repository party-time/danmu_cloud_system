package cn.partytime.common.util;

/**
 * Created by lENOVO on 2016/10/25.
 */
public enum DanmuTypeEnmu {
    NORMAL_DANMU(0, "普通弹幕", "danmu"),
    MOVICE_DANMU(1, "动画", "vedio"),
    PICTURE_DANMU(2, "图片", "picture"),
    BLING_DANMU(3, "闪光字", "bling"),
    EXPRESSION_DANMU(4, "表情", "expression"),
    MONEY_DANMU(5, "打赏", "money");

    private String cnName;
    private String enName;
    private int index;

    DanmuTypeEnmu(int index, String cnName, String enName) {
        this.cnName = cnName;
        this.enName = enName;
        this.index = index;
    }

    public static String getCnName(int index) {
        for (DanmuTypeEnmu dnmuTypeEnmu : DanmuTypeEnmu.values()) {
            if (dnmuTypeEnmu.getIndex() == index) {
                return dnmuTypeEnmu.getCnName();
            }
        }
        return null;
    }

    public static String getEnName(int index) {
        for (DanmuTypeEnmu dnmuTypeEnmu : DanmuTypeEnmu.values()) {
            if (dnmuTypeEnmu.getIndex() == index) {
                return dnmuTypeEnmu.getEnName();
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
