package cn.partytime.model;

import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.RealTimeDmAddress;

import java.util.List;

/**
 * Created by administrator on 2018/1/3.
 */
public class BmsRealTimeDmAddress {

    private RealTimeDmAddress realTimeDmAddress;

    private List<DanmuAddress> danmuAddressList;

    public RealTimeDmAddress getRealTimeDmAddress() {
        return realTimeDmAddress;
    }

    public void setRealTimeDmAddress(RealTimeDmAddress realTimeDmAddress) {
        this.realTimeDmAddress = realTimeDmAddress;
    }

    public List<DanmuAddress> getDanmuAddressList() {
        return danmuAddressList;
    }

    public void setDanmuAddressList(List<DanmuAddress> danmuAddressList) {
        this.danmuAddressList = danmuAddressList;
    }
}
