package cn.partytime.service;

import cn.partytime.model.DanmuLogicModel;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.ReportListModel;
import cn.partytime.model.danmu.Danmu;
import cn.partytime.model.manager.Report;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.service.wechat.WechatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2017/8/29.
 */
@Service
public class BmsReportService {

    @Autowired
    private ReportService reportService;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private BmsDanmuService bmsDanmuService;

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
            List<DanmuLogicModel> danmuList = bmsDanmuService.findDanmuListByIdList(danmuIdList);
            List<ReportListModel> reportListModelList = new ArrayList<>();
            for(Report report : reportPage.getContent()){
                ReportListModel reportListModel = new ReportListModel();
                reportListModel.setReport(report);
                for(DanmuLogicModel danmu : danmuList){
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

    public String reportDanmu(String openId,String danmuId){
        WechatUser wechatUser = wechatUserService.findByOpenId(openId);
        Danmu danmu = danmuService.findById(danmuId);
        if( null != wechatUser && null != danmu){
            Report report = reportService.findByWechatIdAndDanmuId(wechatUser.getId(),danmuId);
            if( null == report){
                report = new Report();
                report.setDanmuId(danmuId);
                report.setWechatId(wechatUser.getId());
                reportService.save(report);
                return null;
            }else{
                return "请不要重复举报";
            }
        }
        return "参数错误";
    }


}
