package cn.partytime.controller;

import cn.partytime.model.PageResultModel;
import cn.partytime.service.BmsReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by administrator on 2017/8/29.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/report")
@Slf4j
public class ReportController {

    @Autowired
    private BmsReportService bmsReportService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel reportPage(Integer pageNumber, Integer pageSize) {
        return bmsReportService.findAll(pageNumber-1,pageSize);
    }

}
