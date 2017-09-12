package cn.partytime.logicService;


import cn.partytime.cache.danmu.PreDanmuCacheService;
import cn.partytime.cache.danmu.PreDanmuLibraryCacheService;
import cn.partytime.cache.party.PartyCacheService;
import cn.partytime.common.cachekey.party.PartyCacheKey;
import cn.partytime.common.util.ListUtils;
import cn.partytime.common.util.SetUtils;
import cn.partytime.model.CmdTempAllData;
import cn.partytime.model.CmdTempComponentData;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.PreDanmuModel;
import cn.partytime.model.danmu.DanmuLibraryParty;
import cn.partytime.model.danmu.PreDanmu;
import cn.partytime.repository.danmu.DanmuLibraryPartyRepository;
import cn.partytime.service.PartyService;
import cn.partytime.service.PreDanmuService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class PreDanmuLogicService {

    @Autowired
    private DanmuLibraryPartyRepository danmuLibraryPartyRepository;

    @Autowired
    private PreDanmuService preDanmuService;

    @Autowired
    private CmdLogicService cmdLogicService;

    @Autowired
    private PreDanmuCacheService preDanmuCacheService;

    @Autowired
    private PreDanmuLibraryCacheService preDanmuLibraryCacheService;

    @Autowired
    private PartyCacheService partyCacheService;


    public int getPartyDanmuDensity(String partyId){
        int density = partyCacheService.getPartyDensity(partyId);
        if(density==0){
            density = resetLibraryInfotoCache(partyId);
        }
        return density;
    }

    public void setPreDanmuLibrarySortRule(String partyId){
        resetLibraryInfotoCache(partyId);
    }


    private int resetLibraryInfotoCache(String partyId){
        List<DanmuLibraryParty> danmuLibraryPartyList = danmuLibraryPartyRepository.findByPartyIdOrderByCreateTimeDesc(partyId);
        int sum = 0;
        //String key = PartyCacheKey
        if(ListUtils.checkListIsNotNull(danmuLibraryPartyList)){
            Collections.sort(danmuLibraryPartyList,new Comparator<DanmuLibraryParty>(){
                @Override
                public int compare(DanmuLibraryParty o1, DanmuLibraryParty o2) {
                    return 0;
                }
            });
            for(DanmuLibraryParty danmuLibraryParty:danmuLibraryPartyList){
                sum+=danmuLibraryParty.getDensitry();
                preDanmuLibraryCacheService.setPreDanmuLibraryIntoCache(partyId,danmuLibraryParty.getDanmuLibraryId(),sum,0);
            }
            partyCacheService.setPartyDensity(partyId,sum,0);
        }else{
            preDanmuLibraryCacheService.removePreDanmuLibrary(partyId);
        }

        return sum;

    }

    public Map<String,Object> getPreDanmuFromCache(String partyId,double danmuCount){
        Set<String> stringSet = preDanmuLibraryCacheService.getLibraryIdFromCache(partyId,danmuCount,Double.parseDouble(String.valueOf(100)),0,1,true);
        String libaryId = "";
        log.info("弹幕librarySet:{}",JSON.toJSONString(stringSet));
        if(SetUtils.checkSetIsNotNull(stringSet)){
            for(String str:stringSet){
                libaryId = str;
            }
            Object object = preDanmuCacheService.findPreDanmu(partyId,libaryId);
            if(object==null){
                preDanmuLibraryCacheService.removeDanmuLibraryIdFromCache(partyId,libaryId);
                return getPreDanmuFromCache(partyId,danmuCount);
            }else {
                return ((Map<String,Object>)JSON.parseObject(String.valueOf(object)));
            }
        }
        return null;
    }



    public void initPreDanmu(String partyId){


        resetLibraryInfotoCache(partyId);

        List<DanmuLibraryParty> danmuLibraryPartyList = danmuLibraryPartyRepository.findByPartyIdOrderByCreateTimeDesc(partyId);
        if(ListUtils.checkListIsNotNull(danmuLibraryPartyList)){
            for(DanmuLibraryParty danmuLibraryParty:danmuLibraryPartyList){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        addAllLibraryUnderPartyIntoCache(danmuLibraryParty.getDanmuLibraryId(),danmuLibraryParty.getPartyId());
                    }
                }).start();
            }
        }
    }

    private void addAllLibraryUnderPartyIntoCache(String libraryId,String partyId){
        long count = preDanmuService.countByDanmuLibraryId(libraryId);
        long index = 0;
        int pageSize = 100;
        if (count % pageSize > 0) {
            index = count / pageSize + 1;
        } else {
            index = count / pageSize;
        }
        List<PreDanmuModel> preDanmuModelList = new ArrayList<PreDanmuModel>();
        for(int i=0; i<index; i++){
            Page<PreDanmu> preDanmuModelPage = preDanmuService.findPageByDLId(i,pageSize,libraryId);
            //preDanmuModelList.addAll(preDanmuModelTempList);
            if(preDanmuModelPage.getContent()!=null){
                setPreDanmuIntoCache(partyId,libraryId,preDanmuModelPage.getContent());
            }

        }
    }

    public void setPreDanmuIntoCache(String partyId,String libraryId,List<PreDanmu> preDanmuModelList){
        log.info("加载活动{}的预置弹幕",partyId);
        if (ListUtils.checkListIsNotNull(preDanmuModelList)) {
            log.info("获取预置弹幕的数量:{}",preDanmuModelList.size());
            for(PreDanmu preDanmu:preDanmuModelList){
                log.info("预置弹幕:{}", JSON.toJSONString(preDanmu));
                Map<String,Object> preDanmuMap = new HashMap<String,Object>();
                String templateId = preDanmu.getTemplateId();
                Map<String,Object> contentMap = preDanmu.getContent();
                CmdTempAllData cmdTempAllData = cmdLogicService.findCmdTempAllDataByIdFromCache(templateId);
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
                preDanmuCacheService.addPreDanmuIntoCacheUnderParty(partyId,libraryId, JSON.toJSONString(preDanmuMap));
                //preDanmuModelList.forEach(preDanmuModel -> redisService.setValueToList(preDanmuCacheKey, JSON.toJSONString(preDanmuModel)));
            }
        }else{
            log.info("获取预置弹幕的数量:{}",0);
        }
        //预制弹幕缓存时间
        preDanmuCacheService.setPreDanmuIntoCacheUnderPartyTime(partyId,libraryId, 60 * 60 * 24);
    }
}
