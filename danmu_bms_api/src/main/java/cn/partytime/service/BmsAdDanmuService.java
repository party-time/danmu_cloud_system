package cn.partytime.service;

import cn.partytime.common.util.DateUtils;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.common.util.ListUtils;
import cn.partytime.config.CacheDataRepository;
import cn.partytime.dataRpc.RpcCmdService;
import cn.partytime.model.*;
import cn.partytime.model.danmu.AdDanmuLibrary;
import cn.partytime.model.danmu.AdTimerDanmu;
import cn.partytime.model.danmu.AdTimerDanmuModel;
import cn.partytime.model.manager.AdTimerDanmuFile;
import cn.partytime.model.manager.Party;
import cn.partytime.model.manager.PartyAddressAdRelation;
import cn.partytime.model.manager.ResourceFile;
import cn.partytime.redis.service.RedisService;
import cn.partytime.service.adDanmu.AdDanmuLibraryService;
import cn.partytime.service.adDanmu.AdTimerDanmuFileService;
import cn.partytime.service.adDanmu.AdTimerDanmuService;
import cn.partytime.service.adDanmu.PartyAddressAdRelationService;
import cn.partytime.service.danmuCmd.BmsCmdService;
import cn.partytime.util.DanmuCheckUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * Created by Administrator on 2017/1/16.
 */
@Service
public class BmsAdDanmuService {

    private static final Logger logger = LoggerFactory.getLogger(BmsDanmuService.class);


    @Autowired
    private AdDanmuLibraryService adDanmuLibraryService;

    @Autowired
    private AliyunGreenService aliyunGreenService;

    @Autowired
    private CacheDataRepository cacheDataRepository;

    @Autowired
    private AdTimerDanmuService adTimerDanmuService;

    @Autowired
    private AdTimerDanmuFileService adTimerDanmuFileService;


    @Autowired
    private PartyAddressAdRelationService partyAddressAdRelationService;


    @Autowired
    private PartyService partyService;


    @Value("${adTimerDanmu.adfilePath}")
    private String adfilePath;


    @Autowired
    private ResourceFileService resourceFileService;

    @Autowired
    private DanmuCommonService danmuCommonService;



    @Autowired
    private BmsCmdService bmsCmdService;

    @Autowired
    private RedisService redisService;


    @Autowired
    private RpcCmdService rpcCmdService;



    /**
     * 查询弹幕库列表
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResultModel findAdDanmuLibraryPageResultModel(Integer pageNumber, Integer pageSize, int isDelete) {
        PageResultModel pageResultModel = new PageResultModel();
        Page<AdDanmuLibrary> adDanmuLibraryPage = adDanmuLibraryService.findByIsDeleteLessThan(pageNumber, pageSize, isDelete);
        pageResultModel.setTotal(adDanmuLibraryPage.getTotalElements());
        if (ListUtils.checkListIsNotNull(adDanmuLibraryPage.getContent())) {
            pageResultModel.setRows(adDanmuLibraryPage.getContent());
        }
        return pageResultModel;
    }

    /**
     * 插入广告弹幕库
     *
     * @param name
     * @param adminUserId
     */
    public void insertAdDanmuLibrary(String name, String adminUserId) {
        Date date = DateUtils.getCurrentDate();
        AdDanmuLibrary adDanmuLibrary = new AdDanmuLibrary();
        adDanmuLibrary.setName(name);
        adDanmuLibrary.setCreateUserId(adminUserId);
        adDanmuLibrary.setUpdateUserId(adminUserId);
        adDanmuLibrary.setCreateTime(date);
        adDanmuLibrary.setUpdateTime(date);
        adDanmuLibraryService.insertAdDanmuLibrary(adDanmuLibrary);
    }


    /**
     * 更新弹幕库信息
     *
     * @param id
     * @param name
     * @param adminUserId
     */
    public void updateAdDanmuLibrary(String id, String name, String adminUserId) {
        AdDanmuLibrary adDanmuLibrary = adDanmuLibraryService.findAdDanmuLibraryById(id);
        if (adDanmuLibrary != null) {
            Date date = DateUtils.getCurrentDate();
            adDanmuLibrary.setName(name);
            adDanmuLibrary.setUpdateUserId(adminUserId);
            adDanmuLibrary.setUpdateTime(date);
            adDanmuLibraryService.updateAdDanmuLibrary(adDanmuLibrary);
        }
    }

    /**
     * 删除广告弹幕库
     *
     * @param id
     */
    public void deleteAdDanmuLibrary(String id, String adminUserId) {
        AdDanmuLibrary adDanmuLibrary = adDanmuLibraryService.findAdDanmuLibraryById(id);
        if (adDanmuLibrary != null) {
            adDanmuLibrary.setUpdateUserId(adminUserId);
            adDanmuLibrary.setUpdateTime(DateUtils.getCurrentDate());
            adDanmuLibrary.setIsDelete(1);
            adDanmuLibraryService.updateAdDanmuLibrary(adDanmuLibrary);
        }
    }

    public void recoveryAdDanmuLibrary(String id, String adminUserId) {
        AdDanmuLibrary adDanmuLibrary = adDanmuLibraryService.findAdDanmuLibraryById(id);
        if (adDanmuLibrary != null) {
            adDanmuLibrary.setUpdateUserId(adminUserId);
            adDanmuLibrary.setUpdateTime(DateUtils.getCurrentDate());
            adDanmuLibrary.setIsDelete(0);
            adDanmuLibraryService.updateAdDanmuLibrary(adDanmuLibrary);
        }
    }


    /**
     * 插入广告弹幕
     */
    public void saveAdDanmu(HttpServletRequest request, String userId) {

        //模板编号
        String templateId = request.getParameter("templateId");
        String minutes  = request.getParameter("minutes");
        String seconds  = request.getParameter("seconds");
        String libraryId = request.getParameter("libraryId");


        int minuteInt = IntegerUtils.objectConvertToInt(minutes);
        int secondsInt = IntegerUtils.objectConvertToInt(seconds);

        //通过模板编号。获取模板信息
        CmdTempAllData cmdTempAllData = rpcCmdService.findCmdTempAllDataByIdFromCache(templateId);

        Object msg = null;
        List<CmdTempComponentData> cmdTempComponentDataList = cmdTempAllData.getCmdTempComponentDataList();
        Map<String,Object> map = new HashMap<String,Object>();
        if(ListUtils.checkListIsNotNull(cmdTempComponentDataList)) {
            for (CmdTempComponentData cmdTempComponentData : cmdTempComponentDataList) {

                //获取组件key
                String key = cmdTempComponentData.getKey();


                //组件的id 0无组件 1特效视频 2特效图片 3表情图片
                String componentId = cmdTempComponentData.getComponentId();

                //是否审核
                int isCheck = cmdTempComponentData.getIsCheck();

                //组件的类型 0text 1textarea 2select  3radiobutton 4checkbox
                Integer componentType = cmdTempComponentData.getComponentType();

                //0数字 1布尔值 2字符串 3数组
                Integer type = cmdTempComponentData.getType();



                Object content = null;
                Object msgContent = null;

                boolean specialCompontentBoolean =  danmuCommonService.checkSpecialComponent(componentId);
                if(specialCompontentBoolean && "0".equals(componentId)){
                    map.put(key,cmdTempComponentData.getDefaultValue());
                }else{
                    if(type==3){
                        //显示的内容
                        content = danmuCommonService.setProtocolArrayContent(componentType,request.getParameter(key));
                        msgContent = danmuCommonService.setShowArrayContent(componentType,request.getParameter(key),componentId);
                    }else{
                        msgContent = danmuCommonService.setShowNotArrayContent(request.getParameter(key),componentId,type);
                        content = danmuCommonService.setShowNotArrayContent(request.getParameter(key),key,type);
                    }
                }
                if (isCheck == 0) {
                    //显示内容
                    msg = msgContent;
                }

                map.put(key, content);
            }

            //时间差
            Date date = DateUtils.getCurrentDate();

            AdTimerDanmu adTimerDanmu = new AdTimerDanmu();
            adTimerDanmu.setBeginTime(minuteInt*60+secondsInt);
            adTimerDanmu.setCreateUserId(userId);
            adTimerDanmu.setCreateTime(date);
            adTimerDanmu.setContent(map);
            //timerDanmu.setDataType(dataType);
            adTimerDanmu.setUpdateUserId(userId);
            adTimerDanmu.setLibraryId(libraryId);
            adTimerDanmu.setTemplateId(templateId);
            adTimerDanmu.setUpdateTime(date);

            adTimerDanmuService.insertAdTimerDanmu(adTimerDanmu);
        }
    }

    public void saveVideo(HttpServletRequest request, String userId){
        String templateId = request.getParameter("templateId");
        String minutes  = request.getParameter("videoMinutes");
        String seconds  = request.getParameter("videoSeconds");
        String partyId = request.getParameter("currentPartyId");

        String videoName = request.getParameter("videoName");
        String videoId = request.getParameter("videoId");
        String lastTime = request.getParameter("lastTime");
        String libraryId = request.getParameter("currentlibraryId");


        int minuteInt = IntegerUtils.objectConvertToInt(minutes);
        int secondsInt = IntegerUtils.objectConvertToInt(seconds);

        AdTimerDanmu adTimerDanmu = new AdTimerDanmu();

        //时间差
        Date date = DateUtils.getCurrentDate();

        Map<String,Object> data = new HashMap<String,Object>();
        data.put("idd",videoId);
        data.put("status",0);
        int beginTime =minuteInt*60+secondsInt;
        adTimerDanmu.setBeginTime(beginTime);

        int lastTimeInt = IntegerUtils.objectConvertToInt(lastTime);

        adTimerDanmu.setCreateUserId(userId);
        adTimerDanmu.setCreateTime(date);
        adTimerDanmu.setContent(data);


        adTimerDanmu.setEndTime(beginTime+lastTimeInt);
        //timerDanmu.setDataType(dataType);
        adTimerDanmu.setUpdateUserId(userId);
        adTimerDanmu.setLibraryId(libraryId);
        adTimerDanmu.setTemplateId("0");
        adTimerDanmu.setUpdateTime(date);
        adTimerDanmuService.insertAdTimerDanmu(adTimerDanmu);



    }






    /**
     * 删除告弹幕
     *
     * @return
     */
    public void deleteAdDanmu(String id, String adminUserId) {
        AdTimerDanmu adTimerDanmu = adTimerDanmuService.findAdTimerDanmu(id);
        if (adTimerDanmu != null) {
            adTimerDanmuService.deleteAdTimerDanmu(adTimerDanmu.getId());
        }
    }

    /**
     * 获取弹幕列表
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageResultModel findAdPageResultModel(Integer pageNumber, Integer pageSize, String libraryId) {
        PageResultModel pageResultModel = new PageResultModel();
        Page<AdTimerDanmu> adTimerDanmuPage = adTimerDanmuService.findByLibraryId(libraryId, pageNumber, pageSize);
        pageResultModel.setTotal(adTimerDanmuPage.getTotalElements());
        /*if(ListUtils.checkListIsNotNull(adTimerDanmuPage.getContent())){
            pageResultModel.setRows(adTimerDanmuPage.getContent());
        }
        for (TimerDanmu_Old timerDanmu : timerDanmuList) {
            timerDanmu.setTypeName(DanmuTypeEnmu.getCnName(timerDanmu.getType()));
        }*/
        List<AdTimerDanmu> timerDanmuList = adTimerDanmuPage.getContent();

        List<AdTimerDanmuModel> adTimerDanmuModelList = new ArrayList<AdTimerDanmuModel>();
        if (ListUtils.checkListIsNotNull(timerDanmuList)) {


            for(AdTimerDanmu adTimerDanmu:timerDanmuList){
                AdTimerDanmuModel adTimerDanmuModel = new AdTimerDanmuModel();
                BeanUtils.copyProperties(adTimerDanmu,adTimerDanmuModel);
                String templateId = adTimerDanmu.getTemplateId();
                if(("0".equals(templateId))){
                    adTimerDanmuModel.setTypeName("视频");
                    Map<String,Object> contentMap = adTimerDanmu.getContent();
                    String vedioId = String.valueOf(contentMap.get("idd"));

                    String message = danmuCommonService.getResourceContent(vedioId,3);
                    adTimerDanmuModel.setMsg(message);

                }else{
                    /*CmdTempAllData cmdTempAllData = bmsCmdService.findCmdTempAllDataById(templateId);
                    adTimerDanmuModel.setTypeName(cmdTempAllData.getName());*/
                    CmdTempAllData cmdTempAllData = rpcCmdService.findCmdTempAllDataByIdFromCache(templateId);

                    adTimerDanmuModel.setTypeName(cmdTempAllData.getName());

                    List<CmdTempComponentData> cmdTempComponentDataList = cmdTempAllData.getCmdTempComponentDataList();
                    for(CmdTempComponentData cmdTempComponentData:cmdTempComponentDataList){
                        int isCheck = cmdTempComponentData.getIsCheck();
                        if(isCheck==0){
                            String key = cmdTempComponentData.getKey();
                            String componentId = cmdTempComponentData.getComponentId();
                            int type = cmdTempComponentData.getType();
                            int danmuType = danmuCommonService.getDanmuType(componentId);

                            Object objectMsg = null;
                            String value = adTimerDanmu.getContent().get(key)+"";
                            if(cmdTempComponentData.getType()==3){
                                //显示的内容
                                objectMsg = danmuCommonService.setShowArrayContent(cmdTempComponentData.getComponentType(),value,componentId);
                            }else{
                                objectMsg = danmuCommonService.setShowNotArrayContent(value,componentId,cmdTempComponentData.getType());
                            }
                            adTimerDanmuModel.setMsg(objectMsg);
                            adTimerDanmuModel.setDanmuType(danmuType);
                        }
                    }
                }
                adTimerDanmuModelList.add(adTimerDanmuModel);
            }
            pageResultModel.setRows(adTimerDanmuModelList);
        }
        return pageResultModel;
    }

    public RestResultModel checkDanmuIsOk(String msg, String color) {
        RestResultModel restResultModel = new RestResultModel();
        if (StringUtils.isEmpty(msg) || msg.length() > 40 || StringUtils.isEmpty(color)) {
            restResultModel.setResult(402);
            restResultModel.setResult_msg("msg is empty or too long or color is empty");
            logger.info("消息内容为空，或者太长，或者颜色为空");
            return restResultModel;
        } else if (check(msg)) {
            restResultModel.setResult(403);
            restResultModel.setResult_msg("msg is blocked");
            logger.info("消息含有屏蔽词");
            return restResultModel;
        } else if (aliyunGreenService.blockTextKeyword(msg)) {
            restResultModel.setResult(403);
            restResultModel.setResult_msg("msg is green blocked");
            logger.info("消息被阿里云屏蔽");
            return restResultModel;
        } else if (DanmuCheckUtil.checkDanmuIsAllBlack(msg)) {
            restResultModel.setResult(403);
            restResultModel.setResult_msg("msg format is wrong");
            logger.info("消息格式错误");
            return restResultModel;
        }
        return null;
    }

    /**
     * 检验弹幕是否合法
     *
     * @param msg
     * @return
     */
    public boolean check(String msg) {
        if (StringUtils.isEmpty(msg)) {
            return false;
        }
        return cacheDataRepository.matchBlockKey(msg.toLowerCase());
    }



    public void createTimerFile(String poolId, String adminId) {
        //获取所有弹幕
        List<AdTimerDanmu> timerDanmuFileList = adTimerDanmuService.findByLibraryIdAllTimerDanmu(poolId);
        //生成弹幕文件
        createFile(timerDanmuFileList, adfilePath, adminId, poolId);
    }

    private void createFile(List<AdTimerDanmu> timerDanmuList, String rootPath, String adminId, String pooId) {
        if (ListUtils.checkListIsNotNull(timerDanmuList)) {
            List<Map<String,Object>> danmuList = new ArrayList<Map<String,Object>>();
            for(AdTimerDanmu adTimerDanmu:timerDanmuList){
                Map<String,Object> timerDanmuMap = new HashMap<String,Object>();
                String templateId = adTimerDanmu.getTemplateId();

                if(!"0".equals(templateId)){
                    Map<String,Object> contentMap = adTimerDanmu.getContent();
                    CmdTempAllData cmdTempAllData = rpcCmdService.findCmdTempAllDataByIdFromCache(templateId);
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
                    timerDanmuMap.put("lastTime",(adTimerDanmu.getEndTime()-adTimerDanmu.getBeginTime()));
                    timerDanmuMap.put("data",adTimerDanmu.getContent());
                    timerDanmuMap.put("type","vedio");
                }
                timerDanmuMap.put("beginTime",adTimerDanmu.getBeginTime());
                danmuList.add(timerDanmuMap);
            }

            String value = JSON.toJSONString(danmuList);
            saveTimerFileInfo(rootPath, adminId, pooId, value);
        }
    }

    private void saveTimerFileInfo(String rootPath, String adminId, String pooId, String value) {

        BufferedWriter bw = null;
        try {
            String fileName = pooId + ".json";
            String parentPath = rootPath + File.separator + pooId + File.separator;
            File file = new File(parentPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String filePath = parentPath + fileName;
            bw = new BufferedWriter(new FileWriter(filePath));// 输出新的json文件
            bw.write(value);
            bw.flush();
            String path = File.separator + pooId + File.separator + fileName;
            //保存文件数据
            adTimerDanmuFileService.insertAdTimerDanmuFile(pooId, path, adminId);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }



}
