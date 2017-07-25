package cn.partytime.rpc;

import cn.partytime.common.util.ListUtils;
import cn.partytime.logicService.CmdLogicService;
import cn.partytime.model.AdTimerResource;
import cn.partytime.model.CmdTempAllData;
import cn.partytime.model.CmdTempComponentData;
import cn.partytime.model.TimerDanmuFileLogicModel;
import cn.partytime.model.danmu.AdTimerDanmu;
import cn.partytime.model.manager.AdTimerDanmuFile;
import cn.partytime.model.manager.Party;
import cn.partytime.model.manager.PartyAddressAdRelation;
import cn.partytime.model.manager.ResourceFile;
import cn.partytime.service.*;
import cn.partytime.service.adDanmu.AdTimerDanmuFileService;
import cn.partytime.service.adDanmu.AdTimerDanmuService;
import cn.partytime.service.adDanmu.PartyAddressAdRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dm on 2017/7/11.
 */

@RestController
@RequestMapping("/rpcAdTimer")
public class RpcAdTimerService {


    @Autowired
    private PartyService partyService;

    @Autowired
    private ResourceFileService resourceFileService;

    @Autowired
    private PartyAddressAdRelationService partyAddressAdRelationService;

    @Autowired
    private AdTimerDanmuFileService adTimerDanmuFileService;

    @Autowired
    private AdTimerDanmuService adTimerDanmuService;

    @Autowired
    private CmdLogicService cmdLogicService;

    @RequestMapping(value = "/findTimerDanmuFileList" ,method = RequestMethod.GET)
    public AdTimerResource findTimerDanmuFileList(@RequestParam String addressId) {

        AdTimerResource adTimerResource = new AdTimerResource();

        List<Party> partyList = partyService.findByTypeAndStatus(1, 2);
        List<String> partyIdlList = new ArrayList<String>();
        if (ListUtils.checkListIsNotNull(partyList)) {
            partyList.stream().forEach(party -> partyIdlList.add(party.getId()));
        }
        //通过活动和地址获取弹幕弹幕库
        List<PartyAddressAdRelation> partyAddressAdRelationList = partyAddressAdRelationService.findPartyAddressAdRelationByAddressIdAndPartyIdList(addressId, partyIdlList);

        if(ListUtils.checkListIsNotNull(partyAddressAdRelationList)){
            List<String> poolIdList = new ArrayList<String>();
            //Map<String,String> partyPoolMap = new HashMap<String,String>();
            List<TimerDanmuFileLogicModel> timerDanmuFileLogicModelList = new ArrayList<TimerDanmuFileLogicModel>();
            for(PartyAddressAdRelation partyAddressAdRelation:partyAddressAdRelationList){
                TimerDanmuFileLogicModel timerDanmuFile = new TimerDanmuFileLogicModel();
                poolIdList.add(partyAddressAdRelation.getAdDanmuPoolId());
                timerDanmuFile.setPartyId(partyAddressAdRelation.getPartyId());
                timerDanmuFile.setPoolId(partyAddressAdRelation.getAdDanmuPoolId());
                timerDanmuFileLogicModelList.add(timerDanmuFile);
            }
            Map<String,String> map = new HashMap<String,String>();
            List<AdTimerDanmuFile> timerDanmuFileList = adTimerDanmuFileService.findAdTimerDanmuFileByPoolIdList(poolIdList);
            if(ListUtils.checkListIsNotNull(timerDanmuFileList)){
                for(AdTimerDanmuFile adTimerDanmuFile:timerDanmuFileList){
                    map.put(adTimerDanmuFile.getPoolId(),adTimerDanmuFile.getPath());
                }
            }
            //获取广告列表
            List<String> resourceIdList = new ArrayList<String>();
            List<AdTimerDanmu> adTimerDanmuList =  adTimerDanmuService.findByPoolIdIn(poolIdList);
            /**表情:1;特效图片:2 特效视频:3*/
            if(ListUtils.checkListIsNotNull(adTimerDanmuList)){
                for(AdTimerDanmu adTimerDanmu:adTimerDanmuList){
                    String templateId = adTimerDanmu.getTemplateId();

                    Map<String,Object> contentMap = adTimerDanmu.getContent();
                    if("0".equals(templateId)){
                        //视频弹幕

                        resourceIdList.add(String.valueOf(contentMap.get("idd")));
                    }else{
                        CmdTempAllData cmdTempAllData = cmdLogicService.findCmdTempAllDataByIdFromCache(templateId);
                        List<CmdTempComponentData> cmdTempComponentDataList = cmdTempAllData.getCmdTempComponentDataList();
                        if(ListUtils.checkListIsNotNull(cmdTempComponentDataList)){
                            for(CmdTempComponentData cmdTempComponentData:cmdTempComponentDataList){
                                String componentId = cmdTempComponentData.getComponentId();
                                if("1".equals(componentId) || "2".equals(componentId)){
                                    resourceIdList.add(String.valueOf(contentMap.get("idd")));
                                }
                            }
                        }

                    }
                }
            }

            List<ResourceFile> resourceFileList = resourceFileService.findByIds(resourceIdList);
            List<ResourceFile> resultResourceFileList = new ArrayList<ResourceFile>();
            if(ListUtils.checkListIsNotNull(resourceFileList)){
                for(ResourceFile resourceFile:resourceFileList){
                    int type = resourceFile.getFileType();
                    if(type==1|| type==2 || type==3){
                        resultResourceFileList.add(resourceFile);
                    }
                }
                adTimerResource.setResourceFileList(resultResourceFileList);
            }
            for(TimerDanmuFileLogicModel timerDanmuFile :timerDanmuFileLogicModelList){
                timerDanmuFile.setPath(map.get(timerDanmuFile.getPoolId()));
            }
            adTimerResource.setTimerDanmuFileLogicModels(timerDanmuFileLogicModelList);

        }
        return adTimerResource;
    }
}
