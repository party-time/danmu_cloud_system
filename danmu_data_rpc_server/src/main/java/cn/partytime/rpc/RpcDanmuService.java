package cn.partytime.rpc;

import cn.partytime.common.util.ListUtils;
import cn.partytime.common.util.LocalDateTimeUtils;
import cn.partytime.logicService.CmdLogicService;
import cn.partytime.model.CmdTempAllData;
import cn.partytime.model.CmdTempComponentData;
import cn.partytime.model.danmu.DanmuLog;
import cn.partytime.model.danmu.Danmu;
import cn.partytime.model.danmu.DanmuPool;
import cn.partytime.repository.danmu.DanmuRepository;
import cn.partytime.service.DanmuPoolService;
import cn.partytime.service.DanmuService;
import cn.partytime.service.danmu.DanmuLogService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dm on 2017/7/10.
 */

@RestController
@RequestMapping("/rpcDanmu")
@Slf4j
public class RpcDanmuService {

    private static final Logger logger = LoggerFactory.getLogger(RpcDanmuService.class);


    @Autowired
    private DanmuLogService danmuLogService;


    @Autowired
    private DanmuService danmuService;

    @Autowired
    private DanmuRepository danmuRepository;

    @Autowired
    private DanmuPoolService danmuPoolService;

    @Autowired
    private CmdLogicService cmdLogicService;


    @RequestMapping(value = "/setSendUserTime" ,method = RequestMethod.GET)
    public void setSendUserTime(@RequestParam String danmuId) {
        Danmu danmu = danmuService.findById(danmuId);
        if (danmu != null) {
            danmu.setSendUserTime(LocalDateTimeUtils.convertLDTToDate(LocalDateTime.now()));
            danmuService.save(danmu);
        }
    }

    @RequestMapping(value = "/setAdminAccepetTime" ,method = RequestMethod.GET)
    public void setAdminAccepetTime(@RequestParam String danmuLogId) {

        DanmuLog danmuLog =  danmuLogService.findDanmuLogById(danmuLogId);
        log.info("danmuLog:{}",JSON.toJSONString(danmuLog));


        if(danmuLog!=null){
            String danmuId = danmuLog.getDanmuId();
            Danmu danmu = danmuService.findById(danmuId);
            if (danmu != null) {
                danmu.setAdminAccepetTime(LocalDateTimeUtils.convertLDTToDate(LocalDateTime.now()));
                danmuService.save(danmu);
            }
        }
    }


    /**
     * 更新弹幕的发送状态
     * @param id
     * @param sendStatus
     * @return
     */
    @RequestMapping(value = "/updateDanmuStatus" ,method = RequestMethod.GET)
    public void updateDanmuStatus(@RequestParam String id,@RequestParam int sendStatus){
        Danmu danmu =  danmuService.findById(id);
        if(danmu!=null) {
            danmu.setSendStatus(sendStatus);
            danmuService.save(danmu);
        }
    }

    @RequestMapping(value = "/danmuLogSave" ,method = RequestMethod.POST)
    public DanmuLog danmuLogSave(@RequestBody DanmuLog danmuLog){
        return  danmuLogService.save(danmuLog);
    }


    @RequestMapping(value = "/findDanmuLogById" ,method = RequestMethod.GET)
    public DanmuLog findDanmuLogById(@RequestParam String id){
        return  danmuLogService.findDanmuLogById(id);
    }

    @RequestMapping(value = "/danmuSave" ,method = RequestMethod.POST)
    public Danmu danmuSave(@RequestBody Danmu danmuModel) {
        return danmuService.save(danmuModel);
    }


    @RequestMapping(value = "/findDanmuById" ,method = RequestMethod.GET)
    public Danmu findById(@RequestParam String id) {
        return danmuService.findById(id);
    }

    @RequestMapping(value = "/findDanmuByIsBlocked" ,method = RequestMethod.GET)
    public List<Danmu> findDanmuByIsBlocked(@RequestParam int page, @RequestParam int size, @RequestParam boolean isBlocked){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        Page<Danmu> danmuModelPage = danmuRepository.findByIsBlockedAndTemplateIdKey(isBlocked,"pDanmu",pageRequest);
        return danmuModelPage.getContent();
    }


    @RequestMapping(value = "/findDanmuPoolIdListByPartyIdAndAddressId" ,method = RequestMethod.GET)
    public List<String> findDanmuPoolIdListByPartyIdAndAddressId(@RequestParam String partyId,String addressId){
        List<String> addressIdList = new ArrayList<String>();
        addressIdList.add(addressId);
        List<DanmuPool>  danmuPoolList =  danmuPoolService.findByPartyIdAndAddressList(partyId,addressIdList);
        List<String> poolIdList = new ArrayList<String>();
        danmuPoolList.forEach(pool->poolIdList.add(pool.getId()));
        return poolIdList;
    }
    @RequestMapping(value = "/countByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlgAndTimeGreaterThan" ,method = RequestMethod.GET)
    public long countByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlgAndTimeGreaterThan(@RequestParam List<String> danmuPoolIdList,@RequestParam int danmuSrc,boolean isBlocked,@RequestParam long time){
        return danmuRepository.countByDanmuPoolIdInAndDanmuSrcAndIsBlockedAndViewFlgAndTimeGreaterThan(danmuPoolIdList,danmuSrc,isBlocked,true,time);
    }


    @RequestMapping(value = "/findHistoryDanmu" ,method = RequestMethod.GET)
    public  List<Map<String,Object>> findHistoryDanmu(@RequestParam String partyId, @RequestParam int count, @RequestParam String id){
        List<DanmuPool> danmuPoolList = danmuPoolService.findByPartyId(partyId);

        List<Map<String,Object>> danmuList = new ArrayList<Map<String,Object>>();
        if (ListUtils.checkListIsNotNull(danmuPoolList)) {
            List<String> poolIdList = new ArrayList<String>();
            danmuPoolList.forEach(e -> poolIdList.add(e.getId()));
            List<Danmu> danmuModelList = danmuService.findByBlockedAndViewFlgAndDanmuPoolIdInOrderByTimeDesc("pDanmu",poolIdList);
            danmuModelList = resultList(danmuModelList,id,count);
            if (ListUtils.checkListIsNotNull(danmuModelList)) {
                for (Danmu danmuModel : danmuModelList) {

                    Map<String,Object> timerDanmuMap = new HashMap<String,Object>();
                    String templateId = danmuModel.getTemplateId();

                    if(!"0".equals(templateId)){
                        Map<String,Object> contentMap = danmuModel.getContent();
                        CmdTempAllData cmdTempAllData = cmdLogicService.findCmdTempAllDataByIdFromCache(templateId);
                        List<CmdTempComponentData> cmdTempComponentDataList = cmdTempAllData.getCmdTempComponentDataList();
                        if(ListUtils.checkListIsNotNull(cmdTempComponentDataList)){
                            for(CmdTempComponentData cmdTempComponentData:cmdTempComponentDataList){
                                String key = cmdTempComponentData.getKey();
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
                            timerDanmuMap.put("data",contentMap);
                            timerDanmuMap.put("type",cmdTempAllData.getKey());
                        }
                    }else{
                        timerDanmuMap.put("data",danmuModel.getContent());
                        timerDanmuMap.put("type","vedio");
                    }
                    timerDanmuMap.put("id",danmuModel.getId());
                    timerDanmuMap.put("beginTime",danmuModel.getTime());
                    danmuList.add(timerDanmuMap);
                }
            }
        }
        return danmuList;
    }

    public static List<Danmu> resultList(List<Danmu> list,String id,int page){
        if(list==null || list.size()==0){
            logger.info("没有历史弹幕");
            return null;
        }
        int max = 0;
        if(StringUtils.isEmpty(id) || "null".equals(id)){
            logger.info("没有弹幕id");
            int length = list.size();
            if(length>page){
                max=page;
            }else{
                max = length;
            }
            return list.subList(0,max);
        }else{
            logger.info("list:{}", JSON.toJSONString(list));
            int index = 0;
            for(int i=0;i<=list.size(); i++){
                if(id.equals(list.get(i).getId())){
                    index = i;
                    break;
                }
            }
            if(list.size()-index>page){
                return list.subList(index+1,index+1+page);
            }else{
                return list.subList(index+1,list.size());
            }
        }
    }

}
