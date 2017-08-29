package cn.partytime.model;

import cn.partytime.model.danmu.Danmu;
import cn.partytime.model.manager.Report;
import cn.partytime.model.wechat.WechatUser;

/**
 * Created by administrator on 2017/8/29.
 */
public class ReportListModel {

    private Report report;

    private DanmuLogicModel danmu;

    private WechatUser wechatUser;

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public DanmuLogicModel getDanmu() {
        return danmu;
    }

    public void setDanmu(DanmuLogicModel danmu) {
        this.danmu = danmu;
    }

    public WechatUser getWechatUser() {
        return wechatUser;
    }

    public void setWechatUser(WechatUser wechatUser) {
        this.wechatUser = wechatUser;
    }
}
