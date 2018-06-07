package cn.partytime.controller;

import cn.partytime.cache.client.ClientInfoCacheService;
import cn.partytime.common.constants.ClientConst;
import cn.partytime.common.util.SetUtils;
import cn.partytime.dataRpc.RpcDanmuClientService;
import cn.partytime.model.DanmuClientInfoModel;
import cn.partytime.model.DanmuClientModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.service.FlashConfigService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by administrator on 2017/9/5.
 */
@RestController
@RequestMapping(value = "/v1/api/javaClient")
@Slf4j
public class FlashClientController {

    @Autowired
    private FlashConfigService flashConfigService;

    @Autowired
    private ClientInfoCacheService clientInfoCacheService;

    @Autowired
    private RpcDanmuClientService rpcDanmuClientService;

    @RequestMapping(value = "/findclientList/{code}", method = RequestMethod.GET)
    public RestResultModel findclientList(@PathVariable("code")String code){
        RestResultModel restResultModel = new RestResultModel();
        List<DanmuClientInfoModel> danmuClientInfoModelList = new ArrayList<DanmuClientInfoModel>();

        DanmuClientModel danmuClientModel =  rpcDanmuClientService.findByRegistCode(code);
        if(danmuClientModel!=null){
            Set<String> stringSet = clientInfoCacheService.findClientRegisterCodeIntoSortSet(danmuClientModel.getAddressId(), ClientConst.CLIENT_TYPE_NODECLIENT);
            log.info("stringSet:{}",JSON.toJSONString(stringSet));
            if(SetUtils.checkSetIsNotNull(stringSet)){
                for(String str:stringSet){
                    String danmuClientInfoModelStr = clientInfoCacheService.findClientFromCache(str,ClientConst.CLIENT_TYPE_NODECLIENT);
                    log.info("danmuClientInfoModelStr:{}",danmuClientInfoModelStr);
                    if(danmuClientInfoModelStr!=null){
                        DanmuClientInfoModel danmuClientInfoModel = JSON.parseObject(danmuClientInfoModelStr, DanmuClientInfoModel.class);
                        danmuClientInfoModelList.add(danmuClientInfoModel);
                    }
                }
            }
            restResultModel.setResult(200);
            restResultModel.setData(danmuClientInfoModelList);
        }else{
            restResultModel.setResult(404);
            restResultModel.setResult_msg("注册码错误!");
        }
        return restResultModel;
    }
    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public String config(String code){
        String jsonStr = null;
        try{
            jsonStr = flashConfigService.createConfig(code);
        }catch (Exception e){
            log.error("",e);
        }

        if(StringUtils.isEmpty(jsonStr)){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result",500);
            return jsonObject.toJSONString();
        }else{
            return jsonStr;
        }


    }

}
