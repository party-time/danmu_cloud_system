package cn.partytime.controller;

import cn.partytime.model.RestResultModel;
import cn.partytime.model.danmu.DanmuLibraryParty;
import cn.partytime.service.DanmuLibraryPartyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/v1/api/admin/danmuLibraryParty")
public class DanmuLibraryPartyController {


    @Autowired
    private DanmuLibraryPartyService danmuLibraryPartyService;

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id){
        RestResultModel restResultModel = new RestResultModel();
       danmuLibraryPartyService.deleteById(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/getAllByPartyId", method = RequestMethod.GET)
    public RestResultModel findPreDanmuLibaryByPartyId(String partyId){
        RestResultModel restResultModel = new RestResultModel();
        List<DanmuLibraryParty> danmuLibraryPartyList =  danmuLibraryPartyService.findByPartyId(partyId);
        restResultModel.setResult(200);
        restResultModel.setData(danmuLibraryPartyList);
        return restResultModel;
    }
}
