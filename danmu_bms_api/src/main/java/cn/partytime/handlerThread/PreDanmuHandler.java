package cn.partytime.handlerThread;

import cn.partytime.common.cachekey.PreDanmuCacheKey;
import cn.partytime.common.util.ListUtils;
import cn.partytime.dataRpc.RpcCmdService;
import cn.partytime.dataRpc.RpcPreDanmuService;
import cn.partytime.model.CmdTempAllData;
import cn.partytime.model.CmdTempComponentData;
import cn.partytime.model.PreDanmuModel;
import cn.partytime.model.danmu.PreDanmu;
import cn.partytime.redis.service.RedisService;
import cn.partytime.service.PreDanmuService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lENOVO on 2016/11/29.
 */

@Component
@Slf4j
public class PreDanmuHandler {

    private static final Logger logger = LoggerFactory.getLogger(PreDanmuHandler.class);

    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @Autowired
    private PreDanmuService preDanmuService;


    @Autowired
    private RedisService redisService;


    @Autowired
    private RpcCmdService rpcCmdService;

    @Autowired
    private RpcPreDanmuService rpcPreDanmuService;


    public void danmuListenHandler(String partyId) {

        List<String> libraryIdList = rpcPreDanmuService.findDanmuLibraryIdByParty(partyId);
        if(ListUtils.checkListIsNotNull(libraryIdList)){
            log.info("未找到弹幕库");
            return;
        }
        libraryIdList.forEach(libraryId->addAllLibraryUnderPartyIntoCache(libraryId,partyId));
    }


    private void addAllLibraryUnderPartyIntoCache(String libraryId,String partyId){
        long count = rpcPreDanmuService.findPreDanmuCountByLibraryId(libraryId);
        long index = 0;
        int pageSize = 100;
        if (count % pageSize > 0) {
            index = count / pageSize + 1;
        } else {
            index = count / pageSize;
        }
        List<PreDanmuModel> preDanmuModelList = new ArrayList<PreDanmuModel>();
        for(int i=0; i<index; i++){
            List<PreDanmuModel> preDanmuModelTempList = rpcPreDanmuService.findPreDanmuByLibraryId(libraryId,i,pageSize);
            //preDanmuModelList.addAll(preDanmuModelTempList);
            setPreDanmuIntoCache(partyId,preDanmuModelTempList);
        }
    }

    public void setPreDanmuIntoCache(String partyId,List<PreDanmuModel> preDanmuModelList){
        log.info("加载活动{}的预置弹幕",partyId);
        String preDanmuCacheKey = PreDanmuCacheKey.PARTY_PREDANMU_CACHE_LIST + partyId;
        if (ListUtils.checkListIsNotNull(preDanmuModelList)) {
            log.info("获取预置弹幕的数量:{}",preDanmuModelList.size());
            for(PreDanmuModel preDanmuModel:preDanmuModelList){
                log.info("预置弹幕:{}",JSON.toJSONString(preDanmuModel));
                Map<String,Object> preDanmuMap = new HashMap<String,Object>();
                String templateId = preDanmuModel.getTemplateId();
                Map<String,Object> contentMap = preDanmuModel.getContent();
                CmdTempAllData cmdTempAllData = rpcCmdService.findCmdTempAllDataByIdFromCache(templateId);
                List<CmdTempComponentData> cmdTempComponentDataList = cmdTempAllData.getCmdTempComponentDataList();
                if(ListUtils.checkListIsNotNull(cmdTempComponentDataList)){
                    for(CmdTempComponentData cmdTempComponentData:cmdTempComponentDataList){
                        String key =cmdTempComponentData.getKey();
                        if(!contentMap.containsKey(key)){
                            int type = cmdTempComponentData.getType();
                            if(type==3){
                                List<Object> list = new ArrayList<Object>();
                                list.add(cmdTempComponentData.getDefaultValue());

                                contentMap.put(key,list);
                            }else{
                                contentMap.put(key,cmdTempComponentData.getDefaultValue());
                            }
                        }
                    }
                    preDanmuMap.put("data",contentMap);
                }
                preDanmuMap.put("type",cmdTempAllData.getKey());
                redisService.setValueToList(preDanmuCacheKey, JSON.toJSONString(preDanmuMap));
                //preDanmuModelList.forEach(preDanmuModel -> redisService.setValueToList(preDanmuCacheKey, JSON.toJSONString(preDanmuModel)));
            }
        }else{
            log.info("获取预置弹幕的数量:{}",0);
        }
        //预制弹幕缓存时间
        redisService.expire(preDanmuCacheKey, 60 * 60 * 24);
    }
}
