package cn.partytime.controller;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.H5Template;
import cn.partytime.model.manager.Party;
import cn.partytime.service.BmsH5TempService;
import cn.partytime.service.H5TemplateService;
import cn.partytime.service.PartyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2017/4/28.
 */
@RestController
@RequestMapping(value = "/v1/api/admin/h5temp")
@Slf4j
public class H5TemplateController {

    @Autowired
    private H5TemplateService h5TemplateService;

    @Autowired
    private BmsH5TempService bmsH5TempService;

    @Autowired
    private PartyService partyService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel findAll(Integer pageNumber, Integer pageSize){
        if( null == pageNumber){
            pageNumber = 0;
        }else{
            pageNumber = pageNumber-1;
        }
        Page<H5Template> h5TemplatePage = h5TemplateService.findAll(pageNumber,pageSize);
        return new PageResultModel(h5TemplatePage);
    }

    @RequestMapping(value = "/pageByParty", method = RequestMethod.GET)
    public PageResultModel findByIsIndexAndPartyId(String partyId,Integer pageNumber,Integer pageSize){
        if( null == pageNumber){
            pageNumber = 0;
        }else{
            pageNumber = pageNumber-1;
        }
        Party party = partyService.findById(partyId);
        if( null != party){
            if( null != party.getH5TempId()){
                PageResultModel pageResultModel = new PageResultModel();
                pageResultModel.setTotal(0);
                return pageResultModel;
            }
        }
        Page<H5Template> h5TemplatePage = h5TemplateService.findByIsIndex(0,pageNumber,pageSize);
        return new PageResultModel(h5TemplatePage);
    }



    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel save(String tempTitle, String h5Url, String html, Integer isIndex, Integer type,
                                Integer isBase, Integer payMoney, String payTitle){
        RestResultModel restResultModel = new RestResultModel();
        bmsH5TempService.save(tempTitle,h5Url,html,isIndex,type,isBase,payMoney,payTitle);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id){
        RestResultModel restResultModel = new RestResultModel();
        h5TemplateService.delById(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RestResultModel get(String id){
        RestResultModel restResultModel = new RestResultModel();
        H5Template h5Template = h5TemplateService.findById(id);
        restResultModel.setResult(200);
        restResultModel.setData(h5Template);
        return restResultModel;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResultModel update(String id, String tempTitle, String h5Url, String html, Integer isIndex, Integer type,
                                  Integer isBase, Integer payMoney , String payTitle){
        RestResultModel restResultModel = new RestResultModel();
        bmsH5TempService.update(id,tempTitle,h5Url,html,isIndex,type,isBase,payMoney,payTitle);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/findByh5Url", method = RequestMethod.GET)
    public RestResultModel findByh5Url(String h5Url){
        RestResultModel restResultModel = new RestResultModel();
        H5Template h5Template = h5TemplateService.findByH5Url(h5Url);
        restResultModel.setResult(200);
        restResultModel.setData(h5Template);
        return restResultModel;
    }

    @RequestMapping(value = "/countByIsBase", method = RequestMethod.GET)
    public RestResultModel countByIsBase(String id){
        RestResultModel restResultModel = new RestResultModel();
        Integer count = null;
        if(StringUtils.isEmpty(id)){
            count = h5TemplateService.countByIsBase(0);
        }else{
            count = h5TemplateService.countByIsBaseAndIdNot(0,id);
        }
        restResultModel.setResult(200);
        restResultModel.setData(count);
        return restResultModel;
    }

    @RequestMapping(value = "/countByh5Url", method = RequestMethod.GET)
    public RestResultModel countByh5Url(String h5Url, String id){
        RestResultModel restResultModel = new RestResultModel();
        Integer count = null;
        if(StringUtils.isEmpty(id)){
            count = h5TemplateService.countByH5Url(h5Url);
        }else{
            count = h5TemplateService.countByH5UrlAndIdNot(h5Url,id);
        }
        restResultModel.setResult(200);
        restResultModel.setData(count);
        return restResultModel;
    }

    @RequestMapping(value = "/setH5TempId", method = RequestMethod.GET)
    public RestResultModel setH5TempId(String partyId, String h5TempId) {
        if( "" == h5TempId){
            h5TempId = null;
        }
        partyService.setH5TempId(partyId,h5TempId);
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        return restResultModel;
    }


    @RequestMapping(value = "/findbyPartyId", method = RequestMethod.GET)
    public PageResultModel findbyPartyId(String partyId){
        PageResultModel pageResultModel = new PageResultModel();
        Party party = partyService.findById(partyId);
        H5Template h5Template = null;
        if( null != party && !StringUtils.isEmpty(party.getH5TempId())){
            h5Template = h5TemplateService.findById(party.getH5TempId());
        }
        if( null == h5Template){
            pageResultModel.setTotal(0);
        }else{
            List<H5Template> h5TemplateList = new ArrayList();
            h5TemplateList.add(h5Template);
            pageResultModel.setTotal(1);
            pageResultModel.setRows(h5TemplateList);
        }
        return pageResultModel;
    }

}
