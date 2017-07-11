package cn.partytime.service;

import cn.partytime.common.util.*;
import cn.partytime.dataService.CmdLogicService;
import cn.partytime.model.CmdTempAllData;
import cn.partytime.model.CmdTempComponentData;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.danmu.DanmuModel;
import cn.partytime.model.danmu.PreDanmuModel;
import cn.partytime.service.danmuCmd.BmsCmdService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lENOVO on 2016/8/26.
 */
@Service
public class BmsPreDanmuService {

    private static final Logger logger = LoggerFactory.getLogger(BmsPreDanmuService.class);

    @Autowired
    private DanmuService danmuService;

    @Autowired
    private PreDanmuService preDanmuService;

    @Autowired
    private DanmuLibraryPartyService danmuLibraryPartyService;


    @Value("${danmu.templetPath}")
    private String templetPath;


    @Autowired
    private BmsCmdService bmsCmdService;


    @Autowired
    private DanmuCommonService danmuCommonService;


    @Autowired
    private BmsColorService bmsColorService;

    @Autowired
    private CmdLogicService cmdLogicService;

    /**
     * 讲历史弹幕转换成预置弹幕
     *
     * @param id
     */
    public void savePreDanmuFromHistoryDanmu(String id, String userId, String dlId) {

        DanmuModel danmuModel = danmuService.findById(id);
        if(danmuModel!=null){
            Date now = DateUtils.getCurrentDate();
            PreDanmuModel preDanmuModel= new PreDanmuModel();
            preDanmuModel.setContent(danmuModel.getContent());
            preDanmuModel.setTemplateId(danmuModel.getTemplateId());
            preDanmuModel.setDanmuLibraryId(dlId);
            preDanmuModel.setCreateUserId(userId);
            preDanmuModel.setUpdateUserId(userId);
            preDanmuModel.setCreateTime(now);
            preDanmuModel.setUpdateTime(now);
            preDanmuService.save(preDanmuModel);
        }

    }

    /**
     * 插入新的预置弹幕
     *
     */
    public void savePreDanmu(HttpServletRequest request, String userId) {
        //模板编号
        String templateId = request.getParameter("templateId");
        String danmuLibraryId = request.getParameter("danmuLibraryId");

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



            PreDanmuModel preDanmuModel = new PreDanmuModel();
            Date date = DateUtils.getCurrentDate();
            preDanmuModel.setTemplateId(templateId);
            preDanmuModel.setContent(map);
            preDanmuModel.setDanmuLibraryId(danmuLibraryId);

            preDanmuModel.setCreateUserId(userId);
            preDanmuModel.setCreateTime(date);
            preDanmuModel.setUpdateTime(date);
            preDanmuModel.setUpdateUserId(userId);
            preDanmuService.save(preDanmuModel);
        }
    }

    /**
     * 通过活动编号查询预制弹幕数
     *
     * @param partyId
     * @return
     */
    public long findPreDanmuCountByParty(String partyId) {
        return preDanmuService.findPreDanmuCountByParty(partyId);
    }

    public long findPreDanmuCountByDanmuLibrary(String dlId) {
        return preDanmuService.findPreDanmuCountByDanmuLibrary(dlId);
    }


    public void deletePreDanmu(String id) {
        preDanmuService.deletePreDanmu(id);
    }

    public void deleteDanmuLibrary(String id){
        preDanmuService.delDanmuLibrary(id);
        danmuLibraryPartyService.delDanmuLibraryParty(id);
    }


    public RestResultModel importDanmuDatoToDb(MultipartFile multipartFile, String libraryId, String userId)  throws IOException {
        RestResultModel restResultModel = new RestResultModel();
        UUID uuid = UUID.randomUUID();
        String tempPath = templetPath + File.separator+uuid+".xls";

        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(tempPath)));
        stream.write(multipartFile.getBytes());
        stream.close();


        File tempFile = new File(tempPath);
        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(tempFile));
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
        int maxUumber = hssfSheet.getLastRowNum();
        List<PreDanmuModel> preDanmuModelList = new ArrayList<>();

        String templateId = ComponentKeyConst.P_DANMU;
        CmdTempAllData cmdTempAllData = cmdLogicService.findCmdTempAllDataByIdFromCache(templateId);


        for(int i=maxUumber; i>0; i--) {
            HSSFRow row = hssfSheet.getRow(i);
            int maxColumn = 1;
            int count = checkDataIsFull(row, maxColumn);
            if (count > 0 & count < maxColumn) {
                restResultModel.setResult(407);
                restResultModel.setResult_msg("数据不完整！");
                return restResultModel;
            }

            if (count == maxColumn) {
                String content = row.getCell(0).toString();
                String color = row.getCell(1).toString();


                if(StringUtils.isBlank(color)){
                    color = bmsColorService.getRandomColor();
                }




                Map<String,Object> contentMap = new HashMap<String,Object>();
                String msg = "";
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


                PreDanmuModel preDanmuModel = new PreDanmuModel();
                Date now = DateUtils.getCurrentDate();

                preDanmuModel.setContent(contentMap);
                preDanmuModel.setDanmuLibraryId(libraryId);
                preDanmuModel.setTemplateId(templateId);
                //preDanmuModel.setColor(ColorEnmu.getValue(Integer.parseInt(color)));
                preDanmuModel.setCreateTime(now);
                preDanmuModel.setUpdateTime(now);
                preDanmuModel.setIsDelete(0);

                preDanmuModel.setCreateUserId(userId);
                preDanmuModel.setCreateUserId(userId);

                preDanmuModelList.add(preDanmuModel);
            }
        }

        FileUtils.deleteDir(tempFile);
        if(ListUtils.checkListIsNotNull(preDanmuModelList)){
            for(PreDanmuModel preDanmuModel:preDanmuModelList){
                preDanmuService.save(preDanmuModel);
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


}
