package cn.partytime.service;

import cn.partytime.common.constants.Const;
import cn.partytime.common.util.ListUtils;
import cn.partytime.dataRpc.RpcDanmuAddressService;
import cn.partytime.dataRpc.RpcDanmuClientService;
import cn.partytime.dataRpc.RpcParamService;
import cn.partytime.dataRpc.RpcPartyService;
import cn.partytime.model.*;
import cn.partytime.util.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by administrator on 2017/9/5.
 */
@Slf4j
@Service
public class FlashConfigService {

    @Autowired
    private RpcDanmuClientService rpcDanmuClientService;

    @Autowired
    private RpcPartyService rpcPartyService;

    @Autowired
    private ParamLogicService paramLogicService;

    @Autowired
    private RpcParamService rpcParamService;

    @Autowired
    private RpcDanmuAddressService rpcDanmuAddressService;

    @Autowired
    private ResourceLogicService resourceLogicService;

    @Autowired
    private AdTimerDanmuLogicService adTimerDanmuLogicService;

    @Autowired
    private TimerDanmuLogicService timerDanmuLogicService;

    private String saveFilePath = "resource";

    public String createConfig(String code){
        JSONObject jsonObject= new JSONObject(true);
        jsonObject.put("result",200);
        DanmuClientModel danmuClientModel = rpcDanmuClientService.findByRegistCode(code);
        if( null == danmuClientModel){
            String jsonStr = HttpUtils.httpRequestStr("http://test.party-time.cn/v1/api/javaClient/config?code="+code,"GET",null);
            if( !StringUtils.isEmpty(jsonStr)){
                return jsonStr;
            }else{
                return null;
            }
        }

        //放入服务器端自定义配置表
        String paramJsonStr = paramLogicService.createJson(rpcParamService.findByRegistCode(code));
        if(!StringUtils.isEmpty(paramJsonStr)){
            JSONObject paramJson = JSON.parseObject(paramJsonStr);
            if( null != paramJson){
                for(String key:paramJson.keySet()){
                    jsonObject.put(key,paramJson.get(key));
                }
            }
        }

        DanmuAddressModel danmuAddress = rpcDanmuAddressService.findById( danmuClientModel.getAddressId());
        List<PartyResourceResult> partyResourceResultList1 = null;
        List<PartyResourceResult> partyResourceResultList2 = null;
        //如果是固定场地,下载电影和活动
        if( danmuAddress.getType() == 0){
            partyResourceResultList1 = resourceLogicService.findFilmResource();
            partyResourceResultList2 = resourceLogicService.findPartyResource(danmuClientModel.getAddressId());
            //log.info("全部电影"+partyResourceResultList1.size());
            //log.info("该场地下的活动"+partyResourceResultList2.size());
            //如果是临时场地，只下载活动
        }else if( danmuAddress.getType() == 1){
            partyResourceResultList1 = new ArrayList<>();
            partyResourceResultList2 = resourceLogicService.findPartyResource(danmuClientModel.getAddressId());
        }
        List<PartyResourceResult> partyResourceResultList = new ArrayList<>();
        partyResourceResultList.addAll(partyResourceResultList1);
        partyResourceResultList.addAll(partyResourceResultList2);

        List<TimerDanmuPathModel>  timerDanmuPathModels  =this.findTimerDanmuFile();

        List<PartyJson> partyJsonList = new ArrayList<>();
        for (PartyResourceResult partyResourceResult : partyResourceResultList) {
            List<ResourceFileModel> resourceFileList = partyResourceResult.getResourceFileList();
            List<ResourceJson> bigExpressionList = new ArrayList<>();
            List<ResourceJson> specialImageList = new ArrayList<>();
            List<VideoResourceJson> specialVideoList = new ArrayList<>();
            if (null != resourceFileList) {
                for (ResourceFileModel resourceFile : resourceFileList) {
                    ResourceJson resourceJson = new ResourceJson();
                    resourceJson.setId(resourceFile.getId());
                    resourceJson.setUrl(this.saveFilePath+"/upload"+resourceFile.getFileUrl());
                    if (Const.RESOURCE_EXPRESSIONS == resourceFile.getFileType()) {
                        bigExpressionList.add(resourceJson);
                    } else if (Const.RESOURCE_SPECIAL_IMAGES == resourceFile.getFileType()) {
                        specialImageList.add(resourceJson);
                    } else if (Const.RESOURCE_SPECIAL_VIDEOS == resourceFile.getFileType()) {
                        VideoResourceJson videoResourceJson = new VideoResourceJson();
                        videoResourceJson.setId(resourceFile.getId());
                        videoResourceJson.setType("click");
                        videoResourceJson.setUrl(this.saveFilePath+"/upload"+resourceFile.getFileUrl());
                        specialVideoList.add(videoResourceJson);
                    }
                }
            }
            PartyJson partyJson = new PartyJson();

            if( null != timerDanmuPathModels) {
                for (TimerDanmuPathModel timerDanmuPathModel : timerDanmuPathModels) {
                    if (partyResourceResult.getParty().getId().equals(timerDanmuPathModel.getPartyId())) {
                        if( null != timerDanmuPathModel.getPathList() && timerDanmuPathModel.getPathList().size() > 0){
                            List<ResourceJson> resourceList = new ArrayList<>();
                            for(String url : timerDanmuPathModel.getPathList()){
                                ResourceJson resource = new ResourceJson();
                                resource.setUrl(url);
                                resourceList.add(resource);
                            }
                            partyJson.setTimerDanmuUrl(resourceList);

                        }else{
                            partyJson.setTimerDanmuUrl(new ArrayList<>());
                        }
                    }

                }
            }

            partyJson.setPartyId(partyResourceResult.getParty().getId());
            partyJson.setName(partyResourceResult.getParty().getName());
            partyJson.setMovieAlias(partyResourceResult.getParty().getMovieAlias());
            partyJson.setExpressions(bigExpressionList);
            partyJson.setSpecialEffects(specialImageList);
            partyJson.setLocalVideoUrl(specialVideoList);

            partyJson.setTimerDanmuUrl(new ArrayList<>());
            partyJsonList.add(partyJson);
        }
        jsonObject.put("partys",partyJsonList);

        AdTimerResource adTimerResource = adTimerDanmuLogicService.findTimerDanmuFileList(danmuClientModel.getAddressId());
        if( null != adTimerResource){
            List<TimerDanmuFileModel> timerDanmuFileModelList =adTimerResource.getTimerDanmuFileLogicModels();
            Map<String,String> adTimerMap = new HashMap<String,String>();
            if(ListUtils.checkListIsNotNull(timerDanmuFileModelList)){
                for(TimerDanmuFileModel timerDanmuFileModel:timerDanmuFileModelList){
                    adTimerMap.put(timerDanmuFileModel.getPartyId(),this.saveFilePath+"/adTimerDanmu"+timerDanmuFileModel.getPath());
                }
            }

            List<ResourceFileModel> resourceFiles = adTimerResource.getResourceFileList();
            List<ResourceJson> adExpressionList = new ArrayList<>();
            List<ResourceJson> specialImageList = new ArrayList<>();
            List<VideoResourceJson> specialVideoList = new ArrayList<>();

            if(ListUtils.checkListIsNotNull(resourceFiles)){
                for (ResourceFileModel resourceFile : resourceFiles) {
                    ResourceJson resourceJson = new ResourceJson();
                    resourceJson.setId(resourceFile.getId());
                    resourceJson.setUrl(this.saveFilePath+"/upload"+resourceFile.getFileUrl());
                    if (Const.RESOURCE_EXPRESSIONS == resourceFile.getFileType()) {
                        adExpressionList.add(resourceJson);
                    } else if (Const.RESOURCE_SPECIAL_IMAGES == resourceFile.getFileType()) {
                        specialImageList.add(resourceJson);
                    } else if (Const.RESOURCE_SPECIAL_VIDEOS == resourceFile.getFileType()) {
                        VideoResourceJson videoResourceJson = new VideoResourceJson();
                        videoResourceJson.setId(resourceFile.getId());
                        videoResourceJson.setType("click");
                        videoResourceJson.setUrl(this.saveFilePath+"/upload"+resourceFile.getFileUrl());
                        specialVideoList.add(videoResourceJson);
                    }
                }
            }
            jsonObject.put("adExpressions",adExpressionList);
            jsonObject.put("adSpecialEffects",specialImageList);
            jsonObject.put("adVideoUrl",specialVideoList);
        }

        return jsonObject.toJSONString();
    }

    public List<TimerDanmuPathModel> findTimerDanmuFile () {
        List<TimerDanmuFileModel> timerDanmuFileConfigList  =timerDanmuLogicService.findTimerDanmuFileList();
        Map<String,List<TimerDanmuFileModel>> map = new HashMap<String,List<TimerDanmuFileModel>>();

        List<TimerDanmuPathModel> timerDanmuPathModels = new ArrayList<>();
        if (ListUtils.checkListIsNotNull(timerDanmuFileConfigList)) {
            //timerDanmuPathMap = new HashMap<String,List<String>>();
            for (TimerDanmuFileModel timerDanmuFileModel : timerDanmuFileConfigList) {
                //String fileName = getFileName(timerDanmuFileModel.getPath());
                String partyId = timerDanmuFileModel.getPartyId();
                if (map.containsKey(partyId)) {
                    List<TimerDanmuFileModel> timerDanmuFileModels = map.get(partyId);
                    timerDanmuFileModels.add(timerDanmuFileModel);
                    map.put(partyId, timerDanmuFileModels);
                } else {
                    List<TimerDanmuFileModel> timerDanmuFileModels = new ArrayList<TimerDanmuFileModel>();
                    timerDanmuFileModels.add(timerDanmuFileModel);
                    map.put(partyId, timerDanmuFileModels);
                }
            }
            for (Map.Entry<String, List<TimerDanmuFileModel>> entry : map.entrySet()) {
                TimerDanmuPathModel timerDanmuPathModel = new TimerDanmuPathModel();
                List<TimerDanmuFileModel> pathList = entry.getValue();
                String key = entry.getKey();
                List<String> nameList = new ArrayList<String>();
                for (TimerDanmuFileModel timerDanmuFileModel : pathList) {
                    String path = timerDanmuFileModel.getPath();
                    nameList.add(this.saveFilePath+"/timerDanmu"+path);
                }
                timerDanmuPathModel.setPartyId(key);
                timerDanmuPathModel.setPathList(nameList);
                timerDanmuPathModels.add(timerDanmuPathModel);
            }
            return timerDanmuPathModels;

        }
        return null;
    }
}
