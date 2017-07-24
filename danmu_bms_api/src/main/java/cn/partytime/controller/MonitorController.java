package cn.partytime.controller;

import cn.partytime.logic.danmu.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.monitor.Monitor;
import cn.partytime.service.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by administrator on 2017/6/20.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/monitor")
@Slf4j
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel findAll(Integer pageNumber, Integer pageSize){
        if( null == pageNumber){
            pageNumber = 0;
        }else{
            pageNumber = pageNumber-1;
        }
        Page<Monitor> monitorPage = monitorService.findAll(pageNumber,pageSize);
        return new PageResultModel(monitorPage);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel save(String title, String adminUserIds, String content, String key , String wechatTempId){
        RestResultModel restResultModel = new RestResultModel();
        if(StringUtils.isEmpty(key)){
            restResultModel.setResult(404);
            return restResultModel;
        }

        if(monitorService.countByKey(key)>0){
            restResultModel.setResult(404);
            restResultModel.setResult_msg("key有重复的");
            return restResultModel;
        }

        Monitor monitor = new Monitor();
        monitor.setTitle(title);
        monitor.setAdminUserIds(adminUserIds);
        monitor.setContent(content);
        monitor.setKey(key);
        monitor.setWechatTempId(wechatTempId);
        monitorService.save(monitor);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResultModel update(String id,String title, String adminUserIds, String content, String key, String wechatTempId){
        RestResultModel restResultModel = new RestResultModel();

        Monitor monitor = monitorService.findById(id);
        if( null == monitor){
            restResultModel.setResult(404);
            restResultModel.setResult_msg("报警不存在");
            return restResultModel;
        }

        if( StringUtils.isEmpty(key)){
            restResultModel.setResult(404);
            restResultModel.setResult_msg("报警的key不存在");
            return restResultModel;
        }else{

            if(!key.equals(monitor.getKey())){
                if(monitorService.countByKey(key)>0){
                    restResultModel.setResult(501);
                    restResultModel.setResult_msg("key有重复,请重新填写");
                    return restResultModel;
                }
            }
        }
        monitor.setId(id);
        monitor.setTitle(title);
        monitor.setAdminUserIds(adminUserIds);
        monitor.setContent(content);
        monitor.setKey(key);
        monitor.setWechatTempId(wechatTempId);
        monitorService.update(monitor);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/countByKey", method = RequestMethod.GET)
    public RestResultModel countByKey(String key){
        RestResultModel restResultModel = new RestResultModel();
        Integer count = monitorService.countByKey(key);
        restResultModel.setResult(200);
        restResultModel.setData(count);
        return restResultModel;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RestResultModel get(String id){
        RestResultModel restResultModel = new RestResultModel();
        Monitor monitor = monitorService.findById(id);
        restResultModel.setResult(200);
        restResultModel.setData(monitor);
        return restResultModel;
    }



}
