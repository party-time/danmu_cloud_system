package cn.partytime.common.util;

/**
 * Created by Administrator on 2017/1/9.
 */
public enum  PartyTypeEnmu {
    TYPE_PARTY(0,"party","活动场"),
    TYPE_FILM(1,"film","电影场");
    private String cnName;
    private String enName;
    private int index;

    PartyTypeEnmu(int index,String enName,String cnName ) {
        this.cnName = cnName;
        this.enName = enName;
        this.index = index;
    }
    public static String getCnName(int index) {
        for (PartyTypeEnmu partyTypeEnmu : PartyTypeEnmu.values()) {
            if (partyTypeEnmu.getIndex() == index) {
                return partyTypeEnmu.getCnName();
            }
        }
        return null;
    }

    public static String getEnName(int index) {
        for (PartyTypeEnmu partyTypeEnmu : PartyTypeEnmu.values()) {
            if (partyTypeEnmu.getIndex() == index) {
                return partyTypeEnmu.getEnName();
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
