package cn.partytime.service;

import cn.partytime.common.constants.Const;
import cn.partytime.dataRpc.*;
import cn.partytime.model.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2017/9/5.
 */

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

    private String saveFilePath = "resource";

    public JSONObject createConfig(String code){
        JSONObject jsonObject= new JSONObject(true);

        DanmuClientModel danmuClientModel = rpcDanmuClientService.findByRegistCode(code);
        if( null == danmuClientModel){
            return null;
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
            partyJson.setPartyId(partyResourceResult.getParty().getId());
            partyJson.setName(partyResourceResult.getParty().getName());
            partyJson.setMovieAlias(partyResourceResult.getParty().getMovieAlias());
            partyJson.setExpressions(bigExpressionList);
            partyJson.setSpecialEffects(specialImageList);
            partyJson.setLocalVideoUrl(specialVideoList);
            partyJsonList.add(partyJson);
        }
        jsonObject.put("partys",partyJsonList);


        return jsonObject;
    }
}
