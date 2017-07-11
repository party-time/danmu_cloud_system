package cn.partytime.service;

import cn.partytime.baseModel.GropCountModel;
import cn.partytime.common.util.*;
import cn.partytime.dataService.CmdLogicService;
import cn.partytime.model.*;
import cn.partytime.model.danmu.TimerDanmu;
import cn.partytime.model.danmu.TimerDanmuModel;
import cn.partytime.model.manager.Party;
import cn.partytime.model.manager.TimerDanmuFile;
import cn.partytime.redis.service.RedisService;
import cn.partytime.service.danmuCmd.BmsCmdService;
import com.alibaba.fastjson.JSON;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lENOVO on 2016/10/25.
 */

@Service
public class BmsTimerDanmuService {

    private static final Logger logger = LoggerFactory.getLogger(BmsTimerDanmuService.class);

    @Autowired
    private TimerDanmuService timerDanmuService;


    @Autowired
    private PartyService partyService;


    @Autowired
    private TimerDanmuFileService timerDanmuFileService;

    @Value("${timerDanmu.filePath}")
    private String filePath;

    @Value("${danmu.templetPath}")
    private String uploadTempPath;


    @Autowired
    private BmsCmdService bmsCmdService;


    @Autowired
    private DanmuCommonService danmuCommonService;


    @Autowired
    private RedisService redisService;

    @Autowired
    private BmsColorService bmsColorService;


    @Autowired
    private CmdLogicService cmdLogicService;



    public void saveVideo(HttpServletRequest request, String userId){
        String templateId = request.getParameter("templateId");
        String minutes  = request.getParameter("videoMinutes");
        String seconds  = request.getParameter("videoSeconds");
        String partyId = request.getParameter("currentPartyId");

        String videoName = request.getParameter("videoName");
        String videoId = request.getParameter("videoId");
        String lastTime = request.getParameter("lastTime");

        String vediodanmuId = request.getParameter("vediodanmuId");


        TimerDanmu timerDanmu =timerDanmuService.findTimerDanmu(vediodanmuId);
        if(timerDanmu==null){
            timerDanmu = new TimerDanmu();
        }

        Party party = partyService.findById(partyId);
        Date date = DateUtils.getCurrentDate();

        int beginTime = 0;
        if(StringUtils.isNotBlank(minutes) || StringUtils.isNotBlank(seconds)){
            int minuteInt = IntegerUtils.objectConvertToInt(minutes);
            int secondsInt = IntegerUtils.objectConvertToInt(seconds);
            beginTime = minuteInt*60+secondsInt;
        }else{
            if(party.getDanmuStartTime()!=0){
                long time = (date.getTime()-party.getDanmuStartTime())/1000;
                beginTime = Integer.parseInt(time+"");
            }else{
                beginTime = 0;
            }
        }


        if (party != null) {

            if (timerDanmu.getId() != null) {
                timerDanmu.setCreateUserId(userId);
                timerDanmu.setCreateTime(date);
            }
            //{"data":{"id":"58ae78d60cf23b75ed1eb36e","status":1},"type":"vedio"}

            Map<String,Object> data = new HashMap<String,Object>();
            data.put("idd",videoId);
            data.put("status",0);
            int lastTimeInt = IntegerUtils.objectConvertToInt(lastTime);



            timerDanmu.setBeginTime(beginTime);

            timerDanmu.setEndTime(beginTime+lastTimeInt);
            timerDanmu.setContent(data);
            //timerDanmu.setDataType(dataType);
            timerDanmu.setUpdateUserId(userId);
            timerDanmu.setPartyId(partyId);
            timerDanmu.setTemplateId("0");
            timerDanmu.setUpdateTime(date);
            timerDanmuService.save(timerDanmu);
        }



    }

    public void saveDanmu(HttpServletRequest request, String userId){
        //模板编号
        String templateId = request.getParameter("templateId");
        String minutes  = request.getParameter("minutes");
        String seconds  = request.getParameter("seconds");
        String partyId = request.getParameter("partyId");
        String danmuId = request.getParameter("danmuId");

        Party party = partyService.findById(partyId);



        if(party==null){
            return;
        }

        int beginTime = 0;
        Date date = DateUtils.getCurrentDate();
        if(StringUtils.isNotBlank(minutes) || StringUtils.isNotBlank(seconds)){
            int minuteInt = IntegerUtils.objectConvertToInt(minutes);
            int secondsInt = IntegerUtils.objectConvertToInt(seconds);
            beginTime = minuteInt*60+secondsInt;
        }else{
            if(party.getDanmuStartTime()!=0){
                long time = (date.getTime()-party.getDanmuStartTime())/1000;
                beginTime = Integer.parseInt(time+"");
            }else{
                beginTime = 0;
            }
        }

        //通过模板编号。获取模板信息
        CmdTempAllData cmdTempAllData = cmdLogicService.findCmdTempAllDataByIdFromCache(templateId);

        Object msg = null;
        Integer dataType = null;




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

                //协议内容
                Object content = null;
                //显示的内容
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
                        content = danmuCommonService.setShowNotArrayContent(request.getParameter(key),type);
                    }
                }
                if (isCheck == 0) {
                    //显示内容
                    msg = msgContent;
                }
                if (isCheck == 0) {
                    //显示内容
                    msg = msgContent;
                }

                map.put(key, content);
            }


            TimerDanmu timerDanmu = timerDanmuService.findTimerDanmu(danmuId);
            if(timerDanmu==null){
                timerDanmu = new TimerDanmu();
            }
            timerDanmu.setBeginTime(beginTime);
            if (timerDanmu.getId() != null) {
                timerDanmu.setCreateUserId(userId);
                timerDanmu.setCreateTime(date);
            }
            timerDanmu.setContent(map);
            //timerDanmu.setDataType(dataType);
            timerDanmu.setUpdateUserId(userId);
            timerDanmu.setPartyId(partyId);
            timerDanmu.setTemplateId(templateId);
            timerDanmu.setUpdateTime(date);
            timerDanmuService.save(timerDanmu);
        }

    }



    /**
     * 获取定时弹幕
     *
     * @param pageNumber
     * @param pageSize
     * @param partyId
     * @return
     */
    public PageResultModel findByPartyId(Integer pageNumber, Integer pageSize, String partyId) {
        Page<TimerDanmu> timerDanmuPage = timerDanmuService.findByPartyId(pageNumber, pageSize, partyId);
        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(timerDanmuPage.getTotalElements());

        List<TimerDanmu> timerDanmuList = timerDanmuPage.getContent();


        List<TimerDanmuModel> timerDanmuModels = new ArrayList<>();
        if (ListUtils.checkListIsNotNull(timerDanmuList)) {
            for (TimerDanmu timerDanmu : timerDanmuList) {
                TimerDanmuModel timerDanmuModel = new TimerDanmuModel();
                BeanUtils.copyProperties(timerDanmu,timerDanmuModel);

                String templateId = timerDanmu.getTemplateId();

                Map<String,Object> map = timerDanmuModel.getContent();

                if(("0".equals(templateId))){
                    timerDanmuModel.setTypeName("视频");
                    Map<String,Object> contentMap = timerDanmu.getContent();
                    String vedioId = String.valueOf(contentMap.get("idd"));

                    String message = danmuCommonService.getResourceContent(vedioId,3);
                    timerDanmuModel.setMsg(message);

                }else{
                    CmdTempAllData cmdTempAllData = cmdLogicService.findCmdTempAllDataByIdFromCache(templateId);

                    timerDanmuModel.setTypeName(cmdTempAllData.getName());

                    List<CmdTempComponentData> cmdTempComponentDataList = cmdTempAllData.getCmdTempComponentDataList();
                    for(CmdTempComponentData cmdTempComponentData:cmdTempComponentDataList){
                        int isCheck = cmdTempComponentData.getIsCheck();
                        if(isCheck==0){
                            String key = cmdTempComponentData.getKey();
                            String componentId = cmdTempComponentData.getComponentId();
                            int type = cmdTempComponentData.getType();
                            int danmuType = danmuCommonService.getDanmuType(componentId);

                            Object objectMsg = null;
                            String value = timerDanmu.getContent().get(key)+"";
                            if(cmdTempComponentData.getType()==3){
                                //显示的内容
                                objectMsg = danmuCommonService.setShowArrayContent(cmdTempComponentData.getComponentType(),value,componentId);
                            }else{
                                objectMsg = danmuCommonService.setShowNotArrayContent(value,componentId,type);
                            }
                            timerDanmuModel.setMsg(objectMsg);
                            timerDanmuModel.setDanmuType(danmuType);
                        }
                    }
                }
                timerDanmuModels.add(timerDanmuModel);
            }
            pageResultModel.setRows(timerDanmuModels);
        }
        return pageResultModel;
    }


    public Map<String, Object> findDanmuModelMap(String partyId) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        DBObject dbObject = timerDanmuService.timerDanmuCountGroupByPartyId(partyId);
        if (dbObject != null) {
            Object[] objects = ((BasicDBList) dbObject).toArray();
            List<GropCountModel> gropCountModelList = new ArrayList<GropCountModel>();
            for (Object object : objects) {
                Map map = (Map) object;
                long beginTime = Long.parseLong(String.valueOf(map.get("beginTime")).replace(".0", ""));
                long count = Long.parseLong(String.valueOf(map.get("count")).replace(".0", ""));
                if (count != 0) {
                    GropCountModel gropCountModel = new GropCountModel(beginTime, count);
                    gropCountModelList.add(gropCountModel);
                }
            }
            if (!ListUtils.checkListIsNotNull(gropCountModelList)) {
                return null;
            }

            //对gropCountModelList进行排序
            List<List<Long>> dataList = new ArrayList<List<Long>>();
            Collections.sort(gropCountModelList, new Comparator<GropCountModel>() {
                @Override
                public int compare(GropCountModel o1, GropCountModel o2) {
                    long i = o1.getKey() - o2.getKey();
                    if (i > 0) {
                        return 1;
                    } else if (i < 0) {
                        return -1;
                    }
                    return 0;
                }
            });


            for (GropCountModel gropCountModel : gropCountModelList) {
                List<Long> list = new ArrayList<Long>();
                list.add(gropCountModel.getKey());
                list.add(gropCountModel.getCount());
                dataList.add(list);
            }

            resultMap.put("series", dataList);

            GropCountModel gropCountModel = gropCountModelList.get(gropCountModelList.size() - 1);
            //设置x轴最大值
            long maxXValue = gropCountModel.getKey();
            long xlength = 0;
            if (maxXValue % 600 > 0) {
                xlength = maxXValue / 600 + 1;
            } else {
                xlength = maxXValue / 600;
            }
            long xmax = xlength * 60 * 10;
            resultMap.put("xmax", xmax);

            //获取y轴最大值
            long maxYValue = 0;
            for (GropCountModel gropCountModel2 : gropCountModelList) {
                if (maxYValue < gropCountModel2.getCount()) {
                    maxYValue = gropCountModel2.getCount();
                }
            }
            long ylength = 0;
            if (maxYValue % 5 > 0) {
                ylength = maxYValue / 5 + 1;
            } else {
                ylength = maxYValue / 5;
            }
            long ymax = ylength * 5 * 1;
            resultMap.put("ymax", ymax);

            List<Integer> xArray = new ArrayList<Integer>();
            xArray.add(0);
            for (int i = 1; i <= xlength; i++) {
                xArray.add(i * 10 * 60);
            }
            resultMap.put("xtickPositions", xArray);

            List<Integer> yArray = new ArrayList<Integer>();
            yArray.add(0);

            if (maxYValue > 10) {
                for (int i = 1; i <= ylength; i++) {
                    yArray.add(i * 5);
                }
            } else {
                for (int i = 1; i <= 10; i++) {
                    yArray.add(i);
                }
            }

            resultMap.put("ytickPositions", yArray);
            return resultMap;
        }

        return null;
    }


    public void createTimerDanmuFile(String partyId) {
        logger.info("创建定时弹幕文件---------------start");


        //删除定时弹幕文件
        deleteTimerFileData(partyId);

        int pageSize = 300;
        long count = timerDanmuService.findCountByPartyId(partyId);
        if (count == 0) {
            return;
        }
        String fileName = "";
        long index = 0;
        if (count % pageSize > 0) {
            index = count / pageSize + 1;
        } else {
            index = count / pageSize;
        }

        for(int i=0; i<index; i++){
            Page<TimerDanmu> timerDanmuPage = timerDanmuService.findByPartyIdOrderBytime(i, pageSize, partyId);
            List<TimerDanmu> timerDanmuList = timerDanmuPage.getContent();
            String name ="_"+(i+1);
            createFile(timerDanmuList,partyId,name);
        }
    }

    public void deleteTimerFileData(String partyId){
        //List<TimerDanmuFile> timerDanmuFileList = timerDanmuFileService.findByPartyId(partyId);
        /*if(ListUtils.checkListIsNotNull(timerDanmuFileList)){
            for(TimerDanmuFile timerDanmuFile:timerDanmuFileList){
                File file = new File(filePath+File.separator+timerDanmuFile.getPath());
                if(file.exists()){
                    file.delete();
                }
            }

        }*/

        File file = new File(filePath+File.separator+partyId);
        try {
            org.apache.commons.io.FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //删除所有旧数据
        timerDanmuFileService.deleteOldDataByPartyId(partyId);
    }

    private void createFile(List<TimerDanmu> timerDanmuList, String partyId,String name) {

        if (ListUtils.checkListIsNotNull(timerDanmuList)) {
            String partyTimerFilePath = filePath + File.separator + partyId;
            File file = new File(partyTimerFilePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            List<Map<String,Object>> danmuList = new ArrayList<Map<String,Object>>();
            for(TimerDanmu timerDanmu:timerDanmuList){
                Map<String,Object> timerDanmuMap = new HashMap<String,Object>();
                String templateId = timerDanmu.getTemplateId();

                if(!"0".equals(templateId)){
                    Map<String,Object> contentMap = timerDanmu.getContent();
                    CmdTempAllData cmdTempAllData = cmdLogicService.findCmdTempAllDataByIdFromCache(templateId);
                    List<CmdTempComponentData> cmdTempComponentDataList = cmdTempAllData.getCmdTempComponentDataList();
                    if(ListUtils.checkListIsNotNull(cmdTempComponentDataList)){
                        for(CmdTempComponentData cmdTempComponentData:cmdTempComponentDataList){
                            String key = cmdTempComponentData.getKey();
                            if(!contentMap.containsKey(cmdTempComponentData.getKey())){
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
                    timerDanmuMap.put("lastTime",(timerDanmu.getEndTime()-timerDanmu.getBeginTime()));
                    timerDanmuMap.put("data",timerDanmu.getContent());
                    timerDanmuMap.put("type","vedio");
                }
                timerDanmuMap.put("beginTime",timerDanmu.getBeginTime());
                danmuList.add(timerDanmuMap);
            }

            String value = JSON.toJSONString(danmuList);
            String fileName = partyId+name;
            saveTimerFileInfo(partyId, partyTimerFilePath, value, fileName);
        }
    }

    private void saveTimerFileInfo(String partyId, String partyTimerFilePath, String value, String fileName) {

        BufferedWriter bw = null;
        try {

            fileName = fileName + ".json";
            String filePath = partyTimerFilePath + File.separator + fileName;
            bw = new BufferedWriter(new FileWriter(filePath));// 输出新的json文件
            bw.write(value);
            bw.flush();

            String path = File.separator + partyId + File.separator + fileName;
            TimerDanmuFile timerDanmuFile = new TimerDanmuFile();
            timerDanmuFile.setPartyId(partyId);
            timerDanmuFile.setPath(path);
            timerDanmuFileService.save(timerDanmuFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
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


    public List<ProtocolModel<PotocolTimerDanmu>> findProtocolModelList(List<TimerDanmu> timerDanmuList){
        Integer direction = -1;
        List<ProtocolModel<PotocolTimerDanmu>> potocolTimerDanmuList = new ArrayList<ProtocolModel<PotocolTimerDanmu> >();
        if(ListUtils.checkListIsNotNull(timerDanmuList)){
            for(int i=0; i<timerDanmuList.size(); i++){
                TimerDanmu timerDanmu = timerDanmuList.get(i);
                PotocolTimerDanmu potocolTimerDanmu = new PotocolTimerDanmu();
            }
        }
        return potocolTimerDanmuList;
    }


    public List<TimerDanmuFileLogicModel> findTimerDanmuFileList(String addressId) {
        List<Party> partyList = partyService.findByTypeAndStatus(1, 2);
        List<String> partyIdlList = new ArrayList<String>();
        if (ListUtils.checkListIsNotNull(partyList)) {
            partyList.stream().forEach(party -> partyIdlList.add(party.getId()));
        }
        List<TimerDanmuFile> timerDanmuFileList = timerDanmuFileService.findByPartyId(partyIdlList);
        List<TimerDanmuFileLogicModel> timerDanmuFileLogicModelList = new ArrayList<TimerDanmuFileLogicModel>();
        if (ListUtils.checkListIsNotNull(timerDanmuFileList)) {
            for (TimerDanmuFile timerDanmuFile : timerDanmuFileList) {
                TimerDanmuFileLogicModel timerDanmuFileLogicModel = new TimerDanmuFileLogicModel();
                BeanUtils.copyProperties(timerDanmuFile, timerDanmuFileLogicModel);
                timerDanmuFileLogicModelList.add(timerDanmuFileLogicModel);
            }
        }
        return timerDanmuFileLogicModelList;
    }




    public RestResultModel importDanmuDatoToDb(MultipartFile multipartFile, String partyId, String userId)  throws IOException {
        RestResultModel restResultModel = new RestResultModel();

        UUID uuid = UUID.randomUUID();
        String tempPath = uploadTempPath + File.separator+uuid+".xls";

        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(tempPath)));
        stream.write(multipartFile.getBytes());
        stream.close();


        File tempFile = new File(tempPath);
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(tempFile));
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        int maxUumber = hssfSheet.getLastRowNum();
        List<TimerDanmu> timerDanmuList = new ArrayList<>();

        String templateId = ComponentKeyConst.P_DANMU;
        CmdTempAllData cmdTempAllData = cmdLogicService.findCmdTempAllDataByIdFromCache(templateId);

        for(int i=1; i<maxUumber; i++){
            HSSFRow row = hssfSheet.getRow(i);

            int maxColumn = 2;
            int count = checkDataIsFull(row,maxColumn);
            if(count>0 & count<maxColumn){
                restResultModel = new RestResultModel();
                restResultModel.setResult(407);
                restResultModel.setResult_msg("数据不完整!");
                return  restResultModel;
            }

            if(count==maxColumn){
                restResultModel = checkDataIsOk(row,maxColumn);
                if(restResultModel==null) {

                    String content = row.getCell(0).toString();
                    String time = row.getCell(1).toString();
                    String color = row.getCell(2).toString();
                    //String position = row.getCell(3).toString();

                    if(StringUtils.isBlank(color)){
                        color = bmsColorService.getRandomColor();
                    }

                    int timeInt = Integer.parseInt(time.substring(0,time.lastIndexOf(".")))*60+Integer.parseInt(time.substring(time.lastIndexOf(".")+1));

                    TimerDanmu timerDanmu = new TimerDanmu();

                    String msg = "";
                    Map<String,Object> contentMap = new HashMap<String,Object>();
                    List<CmdTempComponentData> cmdTempComponentDataList = cmdTempAllData.getCmdTempComponentDataList();
                    for(CmdTempComponentData cmdTempComponentData:cmdTempComponentDataList){
                    /*int isCheck = cmdTempComponentData.getIsCheck();
                    if(isCheck==0){
                        msg = content;
                        contentMap.put(cmdTempComponentData.getKey(),content);
                    }else if("color".equals(cmdTempComponentData.getKey())){
                        contentMap.put(cmdTempComponentData.getKey(),color);
                    }else{
                        contentMap.put(cmdTempComponentData.getKey(),cmdTempComponentData.getDefaultValue());
                    }*/
                        String key  = cmdTempComponentData.getKey();
                        int isCheck = cmdTempComponentData.getIsCheck();
                        if(isCheck==0){
                            msg = content;
                        }
                        if(!"message".equals(key) && !"color".equals(key)){
                            contentMap.put(cmdTempComponentData.getKey(),cmdTempComponentData.getDefaultValue());
                        }

                    }


                    contentMap.put("color",color);
                    contentMap.put("message",content);

                    Date nowDate = DateUtils.getCurrentDate();
                    timerDanmu.setBeginTime(timeInt);
                    timerDanmu.setPartyId(partyId);
                    timerDanmu.setContent(contentMap);
                    timerDanmu.setUpdateTime(nowDate);
                    timerDanmu.setCreateTime(nowDate);
                    timerDanmu.setUpdateUserId(userId);
                    timerDanmu.setCreateUserId(userId);
                    //timerDanmu.setType(0);
                    timerDanmu.setTemplateId(templateId);
                    timerDanmuList.add(timerDanmu);


                }else{
                    FileUtils.deleteDir(tempFile);
                    return restResultModel;
                }
            }
        }
        FileUtils.deleteDir(tempFile);
        if(ListUtils.checkListIsNotNull(timerDanmuList)){
            for(TimerDanmu timerDanmu:timerDanmuList){
                timerDanmuService.save(timerDanmu);
            }
            restResultModel = new RestResultModel();
            restResultModel.setResult(200);
            restResultModel.setResult_msg("导入成功！");
        }else{
            restResultModel = new RestResultModel();
            restResultModel.setResult(404);
            restResultModel.setResult_msg("没有数据！");
        }
        return restResultModel;

    }

    public int  checkDataIsFull(HSSFRow row,int maxColumn){
        int count = 0;
        for(int i=0; i<maxColumn; i++){
            if(row!=null){
                HSSFCell hssfCell = row.getCell(i);
                if(hssfCell!=null){
                    String cellValue=replaceBlank(hssfCell.toString());
                    if(!StringUtils.isEmpty(cellValue)){
                        logger.info("没有数据!");
                        count ++;
                    }
                }
            }
        }
        return count;
    }
    public RestResultModel checkDataIsOk(HSSFRow row, int maxColumn){
        RestResultModel restResultModel = new RestResultModel();
        for(int i=0; i<maxColumn; i++){
            if(row!=null){
                HSSFCell hssfCell = row.getCell(i);
                if(hssfCell!=null){
                    String cellValue=replaceBlank(hssfCell.toString());
                    if(i==1){
                        if(!NumberUtils.checkNumberAfterPoint(cellValue,2)){
                            logger.info("时间格式错误");
                            //return false;
                            restResultModel.setResult(402);
                            restResultModel.setResult_msg("时间格式错误！");
                            return  restResultModel;
                        }
                    }
                    /*if(i==2){
                        if(!NumberUtils.checkNumberIsInteger(cellValue)){
                            logger.info("颜色格式错误");
                            restResultModel.setResult(403);
                            restResultModel.setResult_msg("颜色格式错误！");
                            return  restResultModel;
                        }
                        int color = Integer.parseInt(cellValue);
                        if(color<0 || color>8){
                            logger.info("颜色不存在");
                            restResultModel.setResult(404);
                            restResultModel.setResult_msg("你填写的颜色编号不存在！");
                            return  restResultModel;
                        }
                    }*/
                    if(i==3){
                        if(!NumberUtils.checkNumberIsInteger(cellValue)){
                            logger.info("位置格式错误");
                            restResultModel.setResult(405);
                            restResultModel.setResult_msg("位置格式错误！");
                            return  restResultModel;
                        }

                        int color = Integer.parseInt(cellValue);
                        if(color<0 || color>6){
                            logger.info("位置编号不存在");
                            restResultModel.setResult(404);
                            restResultModel.setResult_msg("你填写的位置编号不存在！");
                            return  restResultModel;
                        }
                    }
                }
            }
        }

        return  null;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    public TimerDanmu findTimerDanmu(String id){
        return timerDanmuService.findTimerDanmu(id);
    }
    public void danmuStartOperate(String partyId,int status){
        Party party = partyService.findById(partyId);
        Date date = DateUtils.getCurrentDate();
        if(status==1){
            party.setDanmuStartTime(date.getTime());
        }else{
            party.setDanmuStartTime(0);
        }
        partyService.updateParty(party);
    }

    public void updateDanmuBeginTime(String partyId,int status){
        List<TimerDanmu> timerDanmuList = timerDanmuService.findDanmuAll(partyId);
        if(ListUtils.checkListIsNotNull(timerDanmuList)){
               for(TimerDanmu timerDanmu:timerDanmuList) {
                   Integer beginTime = timerDanmu.getBeginTime();
                   Integer endTime = timerDanmu.getEndTime();
                   int temp = 1;
                   if (status == 1) {
                       temp = 1;
                       timerDanmu.setBeginTime(beginTime + temp);
                       if(endTime!=null){
                           timerDanmu.setEndTime(endTime + temp);
                       }
                   }else{
                       temp = -1;
                       if(beginTime>0){
                           timerDanmu.setBeginTime(beginTime + temp);
                       }

                       if(endTime!=null){
                           if(endTime>0){
                               timerDanmu.setEndTime(endTime + temp);
                           }
                       }
                   }
                   /*if (timerDanmu.getType() == 1) {
                       timerDanmu.setEndTime(endTime + temp);
                   }*/
                   timerDanmuService.save(timerDanmu);
               }
        }
    }

}
