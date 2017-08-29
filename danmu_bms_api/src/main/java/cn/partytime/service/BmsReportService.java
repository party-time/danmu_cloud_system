package cn.partytime.service;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.ReportListModel;
import cn.partytime.model.danmu.Danmu;
import cn.partytime.model.manager.Report;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.service.wechat.WechatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2017/8/29.
 */
public class BmsReportService {

    @Autowired
    private ReportService reportService;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private DanmuService danmuService;

    public PageResultModel findAll(Integer pageNum,Integer pageSize){
        PageResultModel pageResultModel = new PageResultModel();
        Page<Report> reportPage = reportService.findAll(pageNum,pageSize);
        if( null != reportPage){
            List<String> wechatUserIdList = new ArrayList<>();
            List<String> danmuIdList = new ArrayList<>();
            for(Report report : reportPage.getContent()){
                wechatUserIdList.add(report.getWechatId());
                danmuIdList.add(report.getDanmuId());
            }
            List<WechatUser> wechatUserList = wechatUserService.findByIds(wechatUserIdList);
            List<Danmu> danmuList = danmuService.findByIds(danmuIdList);
            List<ReportListModel> reportListModelList = new ArrayList<>();
            for(Report report : reportPage.getContent()){
                ReportListModel reportListModel = new ReportListModel();
                reportListModel.setReport(report);
                for(Danmu danmu : danmuList){
                    if(danmu.getId().equals(report.getDanmuId())){
                        reportListModel.setDanmu(danmu);
                    }
                }
                for(WechatUser wechatUser : wechatUserList){
                    if(wechatUser.getId().equals(report.getWechatId())){
                        reportListModel.setWechatUser(wechatUser);
                    }
                }
                reportListModelList.add(reportListModel);
            }
            pageResultModel.setTotal(reportPage.getTotalElements());
            pageResultModel.setRows(reportListModelList);
        }
        return pageResultModel;
    }


}
