package cn.partytime.controller;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.spider.Spider;
import cn.partytime.service.spider.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by administrator on 2018/1/4.
 */
@RestController
@RequestMapping(value = "/v1/api/admin/spider")
public class SpiderController {

    @Autowired
    private SpiderService spiderService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public PageResultModel spiderList(Integer pageNumber, Integer pageSize) {
        pageNumber = pageNumber-1;
        Page<Spider> spiderPage = spiderService.findAll(pageNumber,pageSize);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setRows(spiderPage.getContent());
        pageResultModel.setTotal(spiderPage.getTotalElements());
        return pageResultModel;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RestResultModel getSpider(String id) {
        RestResultModel restResultModel = new RestResultModel();
        Spider spider = spiderService.findById(id);
        restResultModel.setResult(200);
        restResultModel.setData(spider);
        return restResultModel;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResultModel updateSpider(@ModelAttribute Spider spider) {
        RestResultModel restResultModel = new RestResultModel();
        spiderService.update(spider);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/findMovie", method = RequestMethod.GET)
    public RestResultModel findMovie() {
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setData(spiderService.findByDateGreaterThan(new Date()));
        restResultModel.setResult(200);
        return restResultModel;
    }



}
