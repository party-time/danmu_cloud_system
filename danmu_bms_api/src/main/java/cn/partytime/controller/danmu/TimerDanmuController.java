package cn.partytime.controller.danmu;

import cn.partytime.controller.base.BaseAdminController;
import cn.partytime.logic.danmu.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.danmu.TimerDanmu;
import cn.partytime.service.BmsTimerDanmuService;
import cn.partytime.service.TimerDanmuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * Created by lENOVO on 2016/10/25.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/timerDanmu")
@Slf4j
public class TimerDanmuController extends BaseAdminController {

    @Autowired
    private TimerDanmuService timerDanmuService;

    @Autowired
    private BmsTimerDanmuService bmsTimerDanmuService;


    @Value("${danmu.templetPath}")
    private String templetPath;



    @RequestMapping(value = "/danmuSave", method = RequestMethod.POST)
    public RestResultModel danmuSave(HttpServletRequest request) {

        RestResultModel restResultModel = new RestResultModel();
        bmsTimerDanmuService.saveDanmu(request,getAdminUser().getId());
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/videoSave", method = RequestMethod.POST)
    public RestResultModel videoSave(HttpServletRequest request) {
        RestResultModel restResultModel = new RestResultModel();
        String videoId = request.getParameter("videoId");
        if(StringUtil.isBlank(videoId)){
            restResultModel.setResult(400);
            restResultModel.setResult_msg("请选择视频");
            return restResultModel;
        }


        bmsTimerDanmuService.saveVideo(request,getAdminUser().getId());
        restResultModel.setResult(200);
        return restResultModel;
    }


    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel page(Integer pageNumber, Integer pageSize, String partyId) {
        pageNumber = pageNumber - 1;
        return bmsTimerDanmuService.findByPartyId(pageNumber, pageSize, partyId);
    }

    /**
     * 获取预制弹幕分布图
     * @param partyId
     * @return
     */
    @RequestMapping(value = "/chart", method = RequestMethod.GET)
    public RestResultModel chart(String partyId) {
        RestResultModel restResultModel = new RestResultModel();
        restResultModel.setResult(200);
        restResultModel.setData(bmsTimerDanmuService.findDanmuModelMap(partyId));
        return restResultModel;
    }


    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public RestResultModel delete(String danmuId) {
        RestResultModel restResultModel = new RestResultModel();
        timerDanmuService.delete(danmuId);
        restResultModel.setResult(200);
        restResultModel.setData("OK");
        return restResultModel;
    }

    @RequestMapping(value = "/findTimerDanmu/{id}", method = RequestMethod.GET)
    public RestResultModel findTimerDanmu(@PathVariable("id") String id) {
        RestResultModel restResultModel = new RestResultModel();
        TimerDanmu timerDanmu = bmsTimerDanmuService.findTimerDanmu(id);
        if(timerDanmu!=null){
            restResultModel.setResult(200);
            restResultModel.setData(timerDanmu);
        }else{
            restResultModel.setResult(0);
            restResultModel.setData("OK");
        }
        return restResultModel;
    }

    @RequestMapping(value = "/danmuStart/{partyId}/{status}", method = RequestMethod.GET)
    public RestResultModel creatTimerDanmuFile(@PathVariable("partyId") String partyId,@PathVariable("status") Integer status) {
        RestResultModel restResultModel = new RestResultModel();
        bmsTimerDanmuService.danmuStartOperate(partyId,status);
        restResultModel.setResult(200);
        restResultModel.setData("OK");
        return restResultModel;
    }

    @RequestMapping(value = "/updateDanmuBeginTime/{partyId}/{status}", method = RequestMethod.GET)
    public RestResultModel updateDanmuBeginTime(@PathVariable("partyId") String partyId,@PathVariable("status") Integer status) {
        RestResultModel restResultModel = new RestResultModel();
        bmsTimerDanmuService.updateDanmuBeginTime(partyId,status);
        restResultModel.setResult(200);
        restResultModel.setData("OK");
        return restResultModel;
    }

    @RequestMapping(value = "/creatTimerDanmuFile", method = RequestMethod.GET)
    public RestResultModel creatTimerDanmuFile(String partyId) {
        RestResultModel restResultModel = new RestResultModel();
        bmsTimerDanmuService.createTimerDanmuFile(partyId);
        restResultModel.setResult(200);
        restResultModel.setData("OK");
        return restResultModel;
    }
    @RequestMapping(value = "/downloadTemplet", method = RequestMethod.POST)
    public ResponseEntity<byte[]> downloadTemplet(String partyId) {
        try{
            File file=new File(templetPath+"/timerDanmu.xls");
            if(file.exists()){
                HttpHeaders headers = new HttpHeaders();
                String fileName=new String(("timerDanmu.xls").getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
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


    @RequestMapping(value = "/upload/{partyId}", method = RequestMethod.POST)
    public RestResultModel uploadFile(@RequestParam("uploadFileName") MultipartFile multipartFile,@PathVariable("partyId") String partyId,HttpServletRequest request)  {
        RestResultModel restResultModel = new RestResultModel();

        try{
            if(!multipartFile.isEmpty()){
                //得到文件名称
                String userId = getAdminUser().getId();
                String realFileName = multipartFile.getOriginalFilename();
                String suffix = realFileName.substring(realFileName.indexOf(".")+1, realFileName.length());

                if(!"timerDanmu.xls".equals(realFileName)){
                    restResultModel.setResult(408);
                    restResultModel.setResult_msg("模板错误!");
                    return restResultModel;
                }

                if("xls".equals(suffix)){
                   return bmsTimerDanmuService.importDanmuDatoToDb(multipartFile,partyId,userId);
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
            e.printStackTrace();
            restResultModel.setResult(500);
            restResultModel.setResult_msg("上传文件异常!");
            return restResultModel;
        }
    }
}