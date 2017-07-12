package cn.partytime.rpc;

import cn.partytime.common.util.ListUtils;
import cn.partytime.logicService.CmdLogicService;
import cn.partytime.model.CmdTempAllData;
import cn.partytime.model.CmdTempComponentData;
import cn.partytime.model.danmu.DanmuLog;
import cn.partytime.model.danmu.DanmuModel;
import cn.partytime.model.danmu.DanmuPool;
import cn.partytime.repository.danmu.DanmuRepository;
import cn.partytime.service.DanmuLogService;
import cn.partytime.service.DanmuPoolService;
import cn.partytime.service.DanmuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dm on 2017/7/10.
 */

@RestController
@RequestMapping("/rpcDanmu")
public class RpcDanmuService {



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

    @RequestMapping(value = "/danmuLogSave" ,method = RequestMethod.POST)
    public DanmuLog danmuLogSave(@RequestBody DanmuLog danmuLog){
        return  danmuLogService.save(danmuLog);
    }


    @RequestMapping(value = "/findDanmuLogById" ,method = RequestMethod.GET)
    public DanmuLog findDanmuLogById(@RequestParam String id){
        return  danmuLogService.findDanmuLogById(id);
    }

    @RequestMapping(value = "/danmuSave" ,method = RequestMethod.POST)
    public DanmuModel danmuSave(@RequestBody  DanmuModel danmuModel) {
        return danmuService.save(danmuModel);
    }


    @RequestMapping(value = "/findDanmuById" ,method = RequestMethod.GET)
    public DanmuModel findById(@RequestParam String id) {
        return danmuService.findById(id);
    }

    @RequestMapping(value = "/findDanmuByIsBlocked" ,method = RequestMethod.GET)
    public List<DanmuModel> findDanmuByIsBlocked(@RequestParam int page, @RequestParam int size, @RequestParam boolean isBlocked){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(page, size, sort);
        Page<DanmuModel> danmuModelPage = danmuRepository.findByIsBlocked(isBlocked,pageRequest);
        return danmuModelPage.getContent();
    }

    @RequestMapping(value = "/findHistoryDanmu" ,method = RequestMethod.GET)
    public  List<Map<String,Object>> findHistoryDanmu(@RequestParam String partyId, @RequestParam int time, @RequestParam int count){
        List<DanmuPool> danmuPoolList = danmuPoolService.findByPartyId(partyId);

        List<Map<String,Object>> danmuList = new ArrayList<Map<String,Object>>();
        if (ListUtils.checkListIsNotNull(danmuPoolList)) {
            List<String> poolIdList = new ArrayList<String>();
            danmuPoolList.forEach(e -> poolIdList.add(e.getId()));
            List<DanmuModel> danmuModelList = danmuService.findDanmuListByPartyIdAndTimeAndDanmuPool(partyId, time, poolIdList, count);
            if (ListUtils.checkListIsNotNull(danmuModelList)) {
                for (DanmuModel danmuModel : danmuModelList) {

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
                    timerDanmuMap.put("beginTime",danmuModel.getTime());
                    danmuList.add(timerDanmuMap);
                }
            }
        }
        return danmuList;
    }

}
