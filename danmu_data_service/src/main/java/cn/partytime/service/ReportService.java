package cn.partytime.service;

import cn.partytime.model.manager.Report;
import cn.partytime.repository.manager.ReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Created by administrator on 2017/8/29.
 */
@Service
@Slf4j
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public Report save(Report report){
        return reportRepository.insert(report);
    }

    public Page<Report> findAll(Integer pageNum , Integer pageSize){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(pageNum, pageSize, sort);
        return reportRepository.findAll(pageRequest);
    }

    public Report findById(String id){
        return reportRepository.findOne(id);
    }

    public void updateStatus(String id, Integer status){
        Report report = this.findById(id);
        if(null != report){
            report.setStatus(status);
            reportRepository.save(report);
        }
    }

    public Report findByWechatIdAndDanmuId(String wechatId,String danmuId){
        return reportRepository.findByWechatIdAndDanmuId(wechatId,danmuId);
    }




}
