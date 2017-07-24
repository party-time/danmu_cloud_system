package cn.partytime.controller.shop;

import cn.partytime.model.RestResultModel;
import cn.partytime.model.cms.PageColumn;
import cn.partytime.service.cms.BmsCmsService;
import cn.partytime.service.cms.ColumnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by administrator on 2017/6/29.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/cms")
@Slf4j
public class CmsController {

    @Autowired
    private BmsCmsService bmsCmsService;

    @RequestMapping(value = "/createColumn", method = RequestMethod.POST)
    public RestResultModel createColumn(String url , String title , String addressId ){
        RestResultModel restResultModel = new RestResultModel();
        bmsCmsService.createColumn(url,title,0,addressId);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/getPage", method = RequestMethod.GET)
    public RestResultModel getPage(String url){
        RestResultModel restResultModel = new RestResultModel();
        PageColumn pageColumn = bmsCmsService.findByPageUrl(url);
        restResultModel.setData(pageColumn);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/delColumn", method = RequestMethod.GET)
    public RestResultModel delColumn(String url,String columnId){
        RestResultModel restResultModel = new RestResultModel();
        bmsCmsService.delColumn(url,columnId);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/selectItem", method = RequestMethod.GET)
    public RestResultModel selectItem(String itemId, String columnId){
        RestResultModel restResultModel = new RestResultModel();
        String result = bmsCmsService.selectItem(itemId,columnId);
        if( null != result){
            restResultModel.setResult(501);
            restResultModel.setResult_msg(result);
        }else{
            restResultModel.setResult(200);
        }
        return restResultModel;
    }


    @RequestMapping(value = "/delItem", method = RequestMethod.GET)
    public RestResultModel delItem(String itemId, String columnId){
        RestResultModel restResultModel = new RestResultModel();
        bmsCmsService.delItem(itemId,columnId);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/getPageByAddressId", method = RequestMethod.GET)
    public RestResultModel getPageByAddressId(String addressId){
        RestResultModel restResultModel = new RestResultModel();
        PageColumn pageColumn = bmsCmsService.findByAddressId(addressId);
        restResultModel.setData(pageColumn);
        restResultModel.setResult(200);
        return restResultModel;
    }




}
