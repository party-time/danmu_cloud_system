package cn.partytime.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by liuwei on 2016/9/6.
 */
public class DanmuPoolListResult {

    private String id;

    //一组活动名称
    private List<String> partyNameList;

    //一组场地名称
    private List<String> addressNameList;

    //一组弹幕池编号
    private List<String> danmuPoolIdList;

    private String partyNameListStr;

    private String addressNameListStr;

    public void init(String partyName,String addressName,String danmuPoolId){
        List<String> pl = new ArrayList<String>();
        List<String> al = new ArrayList<String>();
        List<String> dl = new ArrayList<String>();
        pl.add(partyName);
        al.add(addressName);
        dl.add(danmuPoolId);
        this.setAddressNameList(al);
        this.setPartyNameList(pl);
        this.setDanmuPoolIdList(dl);
    }

    public List<String> getPartyNameList() {
        return partyNameList;
    }

    public void setPartyNameList(List<String> partyNameList) {
        this.partyNameList = partyNameList;
    }

    public List<String> getAddressNameList() {
        return addressNameList;
    }

    public void setAddressNameList(List<String> addressNameList) {
        this.addressNameList = addressNameList;
    }

    public List<String> getDanmuPoolIdList() {
        return danmuPoolIdList;
    }

    public void setDanmuPoolIdList(List<String> danmuPoolIdList) {
        this.danmuPoolIdList = danmuPoolIdList;
    }

    public String getPartyNameListStr() {
        if( null != this.getPartyNameList() && this.getPartyNameList().size() > 0){
            String pn = "";
            List<String> list = new ArrayList<String>(new HashSet<String>(this.getPartyNameList()));
            for(String partyName : list){
                pn += partyName + ",";
            }
            return pn.substring(0,pn.length()-1);
        }else{
            return partyNameListStr;
        }

    }

    public void setPartyNameListStr(String partyNameListStr) {
        this.partyNameListStr = partyNameListStr;
    }

    public String getAddressNameListStr() {
        if( null != this.getAddressNameList() && this.getAddressNameList().size() > 0){
            String an = "";
            for(String addressName : this.getAddressNameList()){
                an += addressName + ",";
            }
            return an.substring(0,an.length()-1);
        }else{
            return addressNameListStr;
        }
    }

    public void setAddressNameListStr(String addressNameListStr) {
        this.addressNameListStr = addressNameListStr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
