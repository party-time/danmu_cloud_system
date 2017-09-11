package cn.partytime.controller;

import cn.partytime.common.util.ListUtils;
import cn.partytime.model.DanmuLibraryPartyModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.danmu.DanmuLibrary;
import cn.partytime.model.danmu.DanmuLibraryParty;
import cn.partytime.service.DanmuLibraryPartyService;
import cn.partytime.service.DanmuLibraryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/v1/api/admin/danmuLibraryParty")
public class DanmuLibraryPartyController {


    @Autowired
    private DanmuLibraryPartyService danmuLibraryPartyService;

    @Autowired
    private DanmuLibraryService danmuLibraryService;

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id){
        RestResultModel restResultModel = new RestResultModel();
       danmuLibraryPartyService.deleteById(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/chageDensity", method = RequestMethod.POST)
    public RestResultModel chageDensity(@RequestBody List<DanmuLibraryPartyModel> danmuLibraryPartyModelList){

        RestResultModel restResultModel = new RestResultModel();
        if(ListUtils.checkListIsNotNull(danmuLibraryPartyModelList)){
            danmuLibraryPartyModelList.forEach(danmuLibraryPartyModel -> danmuLibraryPartyService.save(danmuLibraryPartyModel.getDanmuLibraryId(),danmuLibraryPartyModel.getPartyId(),danmuLibraryPartyModel.getDensitry()));
        }
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/getAllByPartyId", method = RequestMethod.GET)
    public RestResultModel findPreDanmuLibaryByPartyId(String partyId){
        RestResultModel restResultModel = new RestResultModel();
        List<DanmuLibraryParty> danmuLibraryPartyList =  danmuLibraryPartyService.findByPartyId(partyId);

        List<DanmuLibraryPartyModel> danmuLibraryPartyModelList = new ArrayList<DanmuLibraryPartyModel>();
        if(ListUtils.checkListIsNotNull(danmuLibraryPartyList)) {
            for (DanmuLibraryParty danmuLibraryParty : danmuLibraryPartyList) {
                DanmuLibraryPartyModel danmuLibraryPartyModel = new DanmuLibraryPartyModel();
                BeanUtils.copyProperties(danmuLibraryParty, danmuLibraryPartyModel);
                DanmuLibrary danmuLibrary = danmuLibraryService.findById(danmuLibraryParty.getDanmuLibraryId());
                danmuLibraryPartyModel.setName(danmuLibrary.getName());
                danmuLibraryPartyModelList.add(danmuLibraryPartyModel);
            }
        }
        restResultModel.setResult(200);
        restResultModel.setData(danmuLibraryPartyModelList);
        return restResultModel;
    }
}
