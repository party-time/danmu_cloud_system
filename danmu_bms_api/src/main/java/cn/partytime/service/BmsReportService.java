package cn.partytime.service;

import cn.partytime.model.DanmuLogicModel;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.ReportListModel;
import cn.partytime.model.danmu.Danmu;
import cn.partytime.model.manager.Report;
import cn.partytime.model.shop.Item;
import cn.partytime.model.shop.Order;
import cn.partytime.model.wechat.WechatUser;
import cn.partytime.model.wechatMsgTmpl.MsgTmpl;
import cn.partytime.model.wechatMsgTmpl.ValueTmpl;
import cn.partytime.service.wechat.WechatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    private BmsWechatUserService bmsWechatUserService;

    @Value("${reportDanmu.id}")
    private String reportDanmu;

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

    public void blockDanmu(String openId,String danmuId,String reportId){
        danmuService.blockDanmu(danmuId);
        reportService.updateStatus(reportId,0);
        MsgTmpl msgTmpl = new MsgTmpl();
        msgTmpl.setTouser(openId);
        msgTmpl.setTemplate_id(reportDanmu);
        msgTmpl.setTopcolor("#FF0000");
        msgTmpl.setUrl("");
        Map<String,ValueTmpl> content = new HashMap<String,ValueTmpl>();
        ValueTmpl valueTmpl = new ValueTmpl();
        valueTmpl.setValue("审核结果通知");
        content.put("first",valueTmpl);

        ValueTmpl valueTmpl1 = new ValueTmpl();
        valueTmpl1.setValue("伟大的观察者，我们已经对您举报的内容进行了审判！感谢您对弹幕电影的关注，还请您多多支持！");
        content.put("keyword1",valueTmpl1);

        ValueTmpl valueTmpl2 = new ValueTmpl();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        valueTmpl2.setValue(sdf.format(new Date()));
        content.put("keyword2",valueTmpl2);


        ValueTmpl valueTmpl5 = new ValueTmpl();
        valueTmpl5.setValue("如有疑问，可发送消息至公众号，我们会尽快给您回复");
        content.put("remark",valueTmpl5);
        msgTmpl.setData(content);
        bmsWechatUserService.sendMsg(msgTmpl);
    }

    public void noProcess(String openId,String reportId){
        reportService.updateStatus(reportId,1);
        MsgTmpl msgTmpl = new MsgTmpl();
        msgTmpl.setTouser(openId);
        msgTmpl.setTemplate_id(reportDanmu);
        msgTmpl.setTopcolor("#FF0000");
        msgTmpl.setUrl("");
        Map<String,ValueTmpl> content = new HashMap<String,ValueTmpl>();
        ValueTmpl valueTmpl = new ValueTmpl();
        valueTmpl.setValue("审核结果通知");
        content.put("first",valueTmpl);

        ValueTmpl valueTmpl1 = new ValueTmpl();
        valueTmpl1.setValue("Oops！如此有爱的内容也要举报吗，聚聚有点小难过呢T-T。");
        content.put("keyword1",valueTmpl1);

        ValueTmpl valueTmpl2 = new ValueTmpl();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        valueTmpl2.setValue(sdf.format(new Date()));
        content.put("keyword2",valueTmpl2);

        ValueTmpl valueTmpl5 = new ValueTmpl();
        valueTmpl5.setValue("如有疑问，可发送消息至公众号，我们会尽快给您回复");
        content.put("remark",valueTmpl5);
        msgTmpl.setData(content);
        bmsWechatUserService.sendMsg(msgTmpl);
    }


}
