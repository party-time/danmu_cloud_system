package cn.partytime.controller.danmu;

import cn.partytime.common.util.ListUtils;
import cn.partytime.controller.base.BaseAdminController;
import cn.partytime.dataRpc.RpcCmdService;
import cn.partytime.model.CmdTempAllData;
import cn.partytime.model.CmdTempComponentData;
import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.danmu.*;
import cn.partytime.service.*;
import cn.partytime.service.danmuCmd.BmsCmdService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lENOVO on 2016/8/26.
 */

@Slf4j
@RestController
@RequestMapping(value = "/v1/api/admin")
public class PreDmController  extends BaseAdminController {

    @Autowired
    private DanmuService danmuService;

    @Autowired
    private PreDanmuService preDanmuService;

    @Autowired
    private DanmuLibraryPartyService danmuLibraryPartyService;

    @Autowired
    private BmsPreDanmuService bmsPreDanmuService;

    @Autowired
    private DanmuCommonService danmuCommonService;

    @Value("${danmu.templetPath}")
    private String templetPath;

    @Autowired
    private RpcCmdService rpcCmdService;

    @RequestMapping(value = "/getAllDanmuLibrary", method = RequestMethod.GET)
    public RestResultModel getAllDanmuLibrary() {
        RestResultModel restResultModel = new RestResultModel();
        List<DanmuLibrary> danmuLibraryList = preDanmuService.findAllDanmuLibrary();
        restResultModel.setResult(200);
        restResultModel.setData(danmuLibraryList);
        return restResultModel;
    }

    @RequestMapping(value = "/saveDanmuLibrary", method = RequestMethod.GET)
    public RestResultModel saveDanmuLibrary(String name) {
        RestResultModel restResultModel = new RestResultModel();
        DanmuLibrary danmuLibraryList = preDanmuService.save(name);
        restResultModel.setData(danmuLibraryList);
        return restResultModel;
    }


    @RequestMapping(value = "/preDm/getAllLibraryNotInIds", method = RequestMethod.GET)
    public RestResultModel findPreDanmuLibaryByPartyId(@RequestParam(value = "ids",required = false)String[] ids){
        RestResultModel restResultModel = new RestResultModel();
        List<DanmuLibrary> danmuLibraryList = null;
        if(ids!=null && ids.length>0){
            List<String> idList = new ArrayList<>();
            for(String id:ids){
                idList.add(id);
            }
            danmuLibraryList =preDanmuService.findByIdNotIn(idList);
        }else{
            danmuLibraryList = preDanmuService.findAllDanmuLibrary();
        }
        restResultModel.setResult(200);
        restResultModel.setData(danmuLibraryList);
        return restResultModel;
    }


    @RequestMapping(value = "/historyDMList", method = RequestMethod.GET)
    public PageResultModel historyDMList(String msg, Integer pageNumber, Integer pageSize) {
        pageNumber = pageNumber -1;
        Page<Danmu> danmuModelPage = null;

        long count  = 0;
        List<Danmu> danmuModelList = null;
        if(StringUtils.isEmpty(msg)){
            danmuModelPage = danmuService.findDanmuListByPage(pageNumber, pageSize);
            danmuModelList = danmuModelPage.getContent();
            count = danmuModelPage.getTotalElements();
        }else{
            PageResultModel<Danmu> danmuModelPageResultModel = danmuService.findByMsgLike(msg,pageNumber,pageSize);
            danmuModelList = danmuModelPageResultModel.getRows();
            count = danmuModelPageResultModel.getTotal();
        }

        /**
         *         long count  = 0;
         if( StringUtils.isEmpty(msg)){
         preDanmuModelPage =  preDanmuService.findPageByDLId(pageNo, pageSize,dlId);
         preDanmuModelList = preDanmuModelPage.getContent();
         count = preDanmuModelPage.getTotalPages();
         }else{
         PageResultModel<PreDanmu> preDanmuModelPageResultModel= preDanmuService.findByDanmuLibraryIdAndMsgLike(pageNo, pageSize,dlId,msg);
         preDanmuModelList = preDanmuModelPageResultModel.getRows();
         count = preDanmuModelPageResultModel.getTotal();
         }
         */

        List<HistoryDanmuModel> historyDanmuModelList = new ArrayList<HistoryDanmuModel>();
        if(ListUtils.checkListIsNotNull(danmuModelList)){
            for(Danmu danmuModel:danmuModelList){
                HistoryDanmuModel historyDanmuModel = new HistoryDanmuModel();
                BeanUtils.copyProperties(danmuModel,historyDanmuModel);

                String templateId = danmuModel.getTemplateId();

                CmdTempAllData cmdTempAllData = rpcCmdService.findCmdTempAllDataByIdFromCache(templateId);

                List<CmdTempComponentData> cmdTempComponentDataList = cmdTempAllData.getCmdTempComponentDataList();
                for(CmdTempComponentData cmdTempComponentData:cmdTempComponentDataList){
                    int isCheck = cmdTempComponentData.getIsCheck();
                    if(isCheck==0){
                        String key = cmdTempComponentData.getKey();
                        String componentId = cmdTempComponentData.getComponentId();



                        Object objectMsg = null;
                        String value = danmuModel.getContent().get(key)+"";
                        if(cmdTempComponentData.getType()==3){
                            //显示的内容
                            objectMsg = danmuCommonService.setShowArrayContent(cmdTempComponentData.getComponentType(),value,componentId,cmdTempComponentData.getDefaultValue());
                        }else{
                            objectMsg = danmuCommonService.setShowNotArrayContent(value,componentId,cmdTempComponentData.getType());
                        }
                        historyDanmuModel.setMsg(objectMsg);

                        int type = cmdTempComponentData.getType();
                        int danmuType = danmuCommonService.getDanmuType(componentId);
                        historyDanmuModel.setDanmuType(danmuType);

                    }
                }
                historyDanmuModelList.add(historyDanmuModel);

            }
        }

        PageResultModel pageResultModel = new PageResultModel();

        pageResultModel.setTotal(count);


        pageResultModel.setRows(historyDanmuModelList);
        return pageResultModel;
    }


    /**
     * TODO:根据活动编号查询
     * 总预制弹幕数量
     * @return
     */
    @RequestMapping(value = "/countPreDM", method = RequestMethod.GET)
    public RestResultModel countPreDM(String dlId) {
        RestResultModel restResultModel = new RestResultModel();
        try {
            long count = bmsPreDanmuService.findPreDanmuCountByDanmuLibrary(dlId);
            restResultModel.setResult(200);
            restResultModel.setResult_msg(count + "");
        } catch (Exception e) {
            restResultModel.setResult(501);
            restResultModel.setResult_msg("error");
        }
        return restResultModel;
    }


    /**
     * 查询预置弹幕
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/preDMList", method = RequestMethod.GET)
    public RestResultModel preDMList(Integer pageNo, Integer pageSize,String dlId,String msg) {
        pageNo = pageNo-1;
        RestResultModel restResultModel = new RestResultModel();
        Page<PreDanmu> preDanmuModelPage = null;
        List<PreDanmu> preDanmuList;
        long count  = 0;
        if( StringUtils.isEmpty(msg)){
            preDanmuModelPage =  preDanmuService.findPageByDLId(pageNo, pageSize,dlId);
            preDanmuList = preDanmuModelPage.getContent();
            count = preDanmuModelPage.getTotalElements();
        }else{
            PageResultModel<PreDanmu> preDanmuModelPageResultModel= preDanmuService.findByDanmuLibraryIdAndMsgLike(pageNo, pageSize,dlId,msg);
            preDanmuList = preDanmuModelPageResultModel.getRows();
            count = preDanmuModelPageResultModel.getTotal();
        }


        List<PreDanmuViewModel> preDanmuViewModelArrayList = new ArrayList<PreDanmuViewModel>();
        if(ListUtils.checkListIsNotNull(preDanmuList)){
            for(PreDanmu preDanmu : preDanmuList){
                PreDanmuViewModel preDanmuViewModel = new PreDanmuViewModel();
                BeanUtils.copyProperties(preDanmu,preDanmuViewModel);

                String templateId = preDanmu.getTemplateId();

                CmdTempAllData cmdTempAllData = rpcCmdService.findCmdTempAllDataByIdFromCache(templateId);

                List<CmdTempComponentData> cmdTempComponentDataList = cmdTempAllData.getCmdTempComponentDataList();
                for(CmdTempComponentData cmdTempComponentData:cmdTempComponentDataList){
                    int isCheck = cmdTempComponentData.getIsCheck();
                    if(isCheck==0){
                        String key = cmdTempComponentData.getKey();
                        String componentId = cmdTempComponentData.getComponentId();
                        int type = cmdTempComponentData.getType();
                        //preDanmuViewModel.setMsg(preDanmuViewModel.getContent().get(key));

                        Object objectMsg = null;
                        String value = preDanmuViewModel.getContent().get(key)+"";
                        if(cmdTempComponentData.getType()==3){
                            //显示的内容
                            objectMsg = danmuCommonService.setShowArrayContent(cmdTempComponentData.getComponentType(),value,componentId,cmdTempComponentData.getDefaultValue());
                        }else{
                            objectMsg = danmuCommonService.setShowNotArrayContent(value,componentId,cmdTempComponentData.getType());
                        }
                        preDanmuViewModel.setMsg(objectMsg);

                        int danmuType = danmuCommonService.getDanmuType(componentId);
                        preDanmuViewModel.setDanmuType(danmuType);

                    }
                }
                preDanmuViewModelArrayList.add(preDanmuViewModel);

            }
        }

        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setTotal(count);
        pageResultModel.setRows(preDanmuViewModelArrayList);
        restResultModel.setResult(200);
        restResultModel.setData(pageResultModel);
        return restResultModel;
    }

    /**
     * 删除预制弹幕
     *
     * @param dmId
     * @param request
     * @return
     */
    @RequestMapping(value = "/deletePreDm", method = RequestMethod.GET)
    public RestResultModel deletePreDm(String dmId, HttpServletRequest request) {
        RestResultModel restResultModel = new RestResultModel();
        try {
            bmsPreDanmuService.deletePreDanmu(dmId);
            restResultModel.setResult(200);
            restResultModel.setResult_msg("success");
        } catch (Exception e) {
            restResultModel.setResult(501);
            restResultModel.setResult_msg("error");
        }
        return restResultModel;
    }


    /**
     * 添加预制弹幕
     * @param  dmId 弹幕编号
     * @param dlId 弹幕库编号
     * @param request
     * @return
     */
    @RequestMapping(value = "/addPreDm", method = RequestMethod.GET)
    public RestResultModel addPreDM(String dmId,String dlId, HttpServletRequest request) {
        RestResultModel restResultModel = new RestResultModel();
        try {
            String userId = getAdminUser().getId();
            bmsPreDanmuService.savePreDanmuFromHistoryDanmu(dmId, userId,dlId);
            restResultModel.setResult(200);
            restResultModel.setResult_msg("success");
        } catch (Exception e) {
            restResultModel.setResult(501);
            restResultModel.setResult_msg("error");
            log.error("",e);
        }
        return restResultModel;

    }

    @RequestMapping(value = "/preDanmu/addNewDanmu", method = RequestMethod.POST)
    public RestResultModel addNewDanmu(HttpServletRequest request) {
        String userId = getAdminUser().getId();
        RestResultModel restResultModel = new RestResultModel();
        try {
            bmsPreDanmuService.savePreDanmu(request,userId);
            restResultModel.setResult(200);
            restResultModel.setResult_msg("success");
        } catch (Exception e) {
            restResultModel.setResult(501);
            restResultModel.setResult_msg("error");
            log.error("",e);
        }
        return restResultModel;
    }

    @RequestMapping(value = "/delDanmuLibrary", method = RequestMethod.GET)
    public RestResultModel delDanmuLibrary(String id) {
        RestResultModel restResultModel = new RestResultModel();
        bmsPreDanmuService.deleteDanmuLibrary(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/preDanmu/downloadTemplet", method = RequestMethod.POST)
    public ResponseEntity<byte[]> downloadTemplet() {
        try{
            File file=new File(templetPath+"/preDanmu.xls");
            if(file.exists()){
                HttpHeaders headers = new HttpHeaders();
                String fileName=new String(("preDanmu.xls").getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
                headers.setContentDispositionFormData("attachment", fileName);
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                        headers, HttpStatus.CREATED);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @RequestMapping(value = "/preDanmu/upload/{id}", method = RequestMethod.POST)
    public RestResultModel uploadFile(@RequestParam("uploadFileName") MultipartFile multipartFile, @PathVariable("id") String id, HttpServletRequest request)  {
        RestResultModel restResultModel = new RestResultModel();

        try{
            if(!multipartFile.isEmpty()){
                //得到文件名称
                String realFileName = multipartFile.getOriginalFilename();
                String suffix = realFileName.substring(realFileName.indexOf(".")+1, realFileName.length());
                if(!"preDanmu.xls".equals(realFileName)){
                    restResultModel.setResult(408);
                    restResultModel.setResult_msg("模板错误!");
                    return restResultModel;
                }

                if("xls".equals(suffix)){
                    String userId = getAdminUser().getId();
                    return bmsPreDanmuService.importDanmuDatoToDb(multipartFile,id,userId);
                }else{
                    restResultModel.setResult(402);
                    restResultModel.setResult_msg("文件格式错误!");
                    return restResultModel;
                }
            }else {
                restResultModel.setResult(404);
                restResultModel.setResult_msg("文件不存在!");
                return restResultModel;
            }
        }catch (Exception e){
            restResultModel.setResult(500);
            restResultModel.setResult_msg("上传文件异常!");
            return restResultModel;
        }
    }


}
