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

        //清除以前的旧的记录
        preDanmuLibraryCacheService.removePreDanmuLibrary(partyId);

        List<DanmuLibraryParty> danmuLibraryPartyList = danmuLibraryPartyRepository.findByPartyIdOrderByCreateTimeAsc(partyId);
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
        }
        return sum;

    }

    public Map<String,Object> getPreDanmuFromCache(String partyId,String addressId,double danmuCount){
        Set<String> stringSet = preDanmuLibraryCacheService.getLibraryIdFromCache(partyId,danmuCount,Double.parseDouble(String.valueOf(100)),0,1,true);
        String libaryId = "";
        log.info("将要从弹幕库:{}中获取弹幕",JSON.toJSONString(stringSet));
        if(SetUtils.checkSetIsNotNull(stringSet)){
            for(String str:stringSet){
                libaryId = str;
            }
            Object object = preDanmuCacheService.findPreDanmu(partyId,addressId,libaryId);
            if(object==null){
                log.info("从弹幕库:{}中获取弹幕:{}",libaryId,object);
                stringSet = preDanmuLibraryCacheService.getAllLibraryIdFromCache(partyId);
                log.info("当前活动的所有预置弹幕库:{}",JSON.toJSONString(stringSet));
                stringSet.remove(libaryId);
                if(SetUtils.checkSetIsNotNull(stringSet)){
                    for(String str:stringSet){
                        object = preDanmuCacheService.findPreDanmu(partyId,addressId,str);
                        if(object!=null){
                            log.info("从弹幕库:{}中没有获取到弹幕,将从:{}中获取弹幕:{}",libaryId,str,object);
                            return ((Map<String,Object>)JSON.parseObject(String.valueOf(object)));
                        }
                    }
                }
            }else{
                return ((Map<String,Object>)JSON.parseObject(String.valueOf(object)));
            }
        }

        return null;
    }


    /**
     * 是否重新加载预置弹幕
     * @param partyId 活动编号
     */
    public void checkIsReInitPreDanmu(String partyId,String addressId){
        log.info("验证是否要重新加载预置弹幕");
        List<DanmuLibraryParty> danmuLibraryPartyList = danmuLibraryPartyRepository.findByPartyIdOrderByCreateTimeAsc(partyId);
        Set<String> stringSet = preDanmuLibraryCacheService.getAllLibraryIdFromCache(partyId);
        if(ListUtils.checkListIsNotNull(danmuLibraryPartyList)){
            Set<String> tempSet = new HashSet<>();
            danmuLibraryPartyList.forEach(temp->tempSet.add(temp.getDanmuLibraryId()));
            if(tempSet.equals(stringSet)){
                log.info("缓存中的预置弹幕库与管理员设置预置弹幕库一致");
                //判断缓存中的每个弹幕库是否有弹幕，如果没有重新加载
                for(String str:tempSet){
                    long size = preDanmuCacheService.getPreDanmuListSize(partyId,addressId,str);
                    if(size==0){
                        log.info("缓存中的预置弹幕库:{}中的弹幕数量是0，需要将此预置弹幕中的弹幕加载到缓存中");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                addAllLibraryUnderPartyIntoCache(str,partyId,addressId);
                            }
                        }).start();
                    }
                }
            }else{
                //重新初始化弹幕缓存队列
                initPreDanmu(partyId,addressId);
            }
        }
    }

    /**
     * 初始化预置弹幕到缓存中
     * @param partyId 活动编号
     * @param addressId  地址编号
     */
    public void initPreDanmu(String partyId,String addressId){

        List<DanmuLibraryParty> danmuLibraryPartyList = danmuLibraryPartyRepository.findByPartyIdOrderByCreateTimeAsc(partyId);
        if(ListUtils.checkListIsNotNull(danmuLibraryPartyList)){
            for(DanmuLibraryParty danmuLibraryParty:danmuLibraryPartyList){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        addAllLibraryUnderPartyIntoCache(danmuLibraryParty.getDanmuLibraryId(),danmuLibraryParty.getPartyId(),addressId);
                    }
                }).start();
            }
        }

        //将预置弹幕库加载到缓存中
        resetLibraryInfotoCache(partyId);
    }

    /**
     * 将预制弹幕加载到缓存中
     * @param libraryId
     * @param partyId
     */
    private void addAllLibraryUnderPartyIntoCache(String libraryId,String partyId,String addressId){
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
            if(preDanmuModelPage.getContent()!=null){
                setPreDanmuIntoCache(partyId,libraryId,preDanmuModelPage.getContent(), addressId);
            }

        }
    }

    public void setPreDanmuIntoCache(String partyId,String libraryId,List<PreDanmu> preDanmuModelList,String addressId){
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
                preDanmuCacheService.addPreDanmuIntoCacheUnderParty(partyId,addressId,libraryId, JSON.toJSONString(preDanmuMap));
            }
        }else{
            log.info("获取预置弹幕的数量:{}",0);
        }
        //预制弹幕缓存时间
        preDanmuCacheService.setPreDanmuIntoCacheUnderPartyTime(partyId,addressId,libraryId, 60 * 60 * 24*7);
    }
}
