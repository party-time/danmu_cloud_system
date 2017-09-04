package cn.partytime.controller;

import cn.partytime.model.RestResultModel;
import cn.partytime.service.BmsPartyResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by administrator on 2017/9/1.
 */

@Controller
@RequestMapping(value = "/v1/api/admin/download")
@Slf4j
public class DownloadPartyResourceController {


    @Autowired
    private BmsPartyResourceService bmsPartyResourceService;


    @RequestMapping(value = "/downloadPartyResource", method = RequestMethod.GET)
    public String downloadPartyResource( String partyId) {
        RestResultModel restResultModel = new RestResultModel();
        String result = bmsPartyResourceService.downLoadPartyResource(partyId);
        if( StringUtils.isEmpty(result)){
            return "error";
        }else{
            return "redirect:/"+result;
        }

    }


}
