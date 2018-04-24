package cn.partytime.controller;

import cn.partytime.common.util.DateUtils;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.client.DanmuClient;
import cn.partytime.service.DanmuClientService;
import cn.partytime.service.DanmuClientListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuwei on 2016/9/7.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/client")
@Slf4j
public class DanmuClientController {

    @Autowired
    private DanmuClientService danmuClientService;

    @Autowired
    private DanmuClientListService danmuClientListService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel danmuClientList(String addressId, int pageNumber, int pageSize){
        pageNumber = pageNumber -1;
        return danmuClientListService.findByAddressId(addressId,pageNumber,pageSize);

    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel danmuClientSave(String addressId,String overdueStr,String name,String paramTemplateId,Integer screenId) throws ParseException {
        RestResultModel restResultModel = new RestResultModel();
        Date overdue = null;
        Date nowDate = DateUtils.getCurrentDate();
        if(!StringUtils.isEmpty(overdueStr)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            overdue = simpleDateFormat.parse(overdueStr+" 23:59:59");
            if(overdue.before(nowDate)){
                restResultModel.setResult(500);
                restResultModel.setData("屏幕有效期,必须大于当前时间");
                return restResultModel;
            }
        }

        //判断屏幕名称是否重复
        DanmuClient danmuClient = danmuClientService.findDanmuClientByAddressIdAndName(addressId,name);
        if(danmuClient==null){
            danmuClient =danmuClientService.save(addressId,overdue,name,paramTemplateId,screenId);
            restResultModel.setResult(200);
            restResultModel.setData(danmuClient);
            return restResultModel;
        }else{
            restResultModel.setResult(500);
            restResultModel.setData("同一个地点下的屏幕名称不能重复!");
            return restResultModel;
        }



    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel danmuClientSave(String id){
        RestResultModel restResultModel = new RestResultModel();
        danmuClientService.deleteById(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/selectParam", method = RequestMethod.GET)
    public RestResultModel selectParam(String id, String paramTemplateId){
        RestResultModel restResultModel = new RestResultModel();
        danmuClientService.selectParamTemplate(id,paramTemplateId);
        restResultModel.setResult(200);
        return restResultModel;
    }


}
