package cn.partytime.controller;

import cn.partytime.model.DanmuClientListResult;
import cn.partytime.model.RestResultModel;
import cn.partytime.service.BmsColorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lENOVO on 2016/10/19.
 */

@RestController
@RequestMapping(value = "/v1/api/admin")
public class BmsColorController {

    @Autowired
    private BmsColorService bmsColorService;

    @RequestMapping(value = "/colors", method = RequestMethod.GET)
    public RestResultModel danmuClientList(String addressId) {
        RestResultModel restResultModel = new RestResultModel();

        Map<String,Object> data = new HashMap<String,Object>();
        data.put("danmuColors",bmsColorService.findDanmuColor(null));
        //data.put("flashWordColors",bmsColorService.findFlashWord());
        restResultModel.setResult(200);
        restResultModel.setData(data);
        return restResultModel;
    }
}
