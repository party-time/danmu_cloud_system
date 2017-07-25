package cn.partytime.controller;


import cn.partytime.model.PageResultModel;
import cn.partytime.model.PartyResourceFileResult;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.Party;
import cn.partytime.model.manager.ResourceFile;
import cn.partytime.service.BmsPartyResourceService;
import cn.partytime.service.PartyResourceService;
import cn.partytime.service.PartyService;
import cn.partytime.service.ResourceFileService;
import cn.partytime.util.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;


/**
 * Created by liuwei on 2016/8/19.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/resource")
@Slf4j
public class ResourceController {

    @Autowired
    private ResourceFileService resourceFileService;

    @Autowired
    private PartyService partyService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Autowired
    private BmsPartyResourceService bmsPartyResourceService;

    @Autowired
    private PartyResourceService partyResourceService;


    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public RestResultModel resourceIndex(String partyId){
        RestResultModel restResultModel = new RestResultModel();
        PartyResourceFileResult partyResourceFileResult = bmsPartyResourceService.findByPartyId(partyId);
        restResultModel.setData(partyResourceFileResult);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel resourceFilePage(Integer fileType, String resourceName, Integer pageNo, Integer pageSize){
        pageNo = pageNo-1;
        Page<ResourceFile> resourceFilePage = null;
        if(StringUtils.isEmpty(resourceName)){
            resourceFilePage= resourceFileService.findAllByType(pageNo,pageSize,fileType);
        }else{
            resourceFilePage=resourceFileService.findAllByType(pageNo,pageSize,fileType,resourceName);
        }

        PageResultModel pageResultModel = new PageResultModel();
        pageResultModel.setRows(resourceFilePage.getContent());
        pageResultModel.setTotal(resourceFilePage.getTotalElements());
        return pageResultModel;
    }

    @RequestMapping(value = "/findResourcePage", method = RequestMethod.GET)
    public Page findResourcePage(Integer fileType, String resourceName,Integer pageNo, Integer pageSize){
        pageNo = pageNo-1;
        Page<ResourceFile> resourceFilePage = null;
        if(StringUtils.isEmpty(resourceName)){
            resourceFilePage= resourceFileService.findAllByType(pageNo,pageSize,fileType);
        }else{
            resourceFilePage=resourceFileService.findAllByType(pageNo,pageSize,fileType,resourceName);
        }
        return resourceFilePage;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public RestResultModel fileUpload(@RequestParam("Filedata") MultipartFile file,String partyId) throws FileUploadException {
        RestResultModel restResultModel = new RestResultModel();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                 Party partyModel = partyService.findById(partyId);
                 if( null != partyModel){
                     ResourceFile resourceFileModel = resourceFileService.save(partyId,file.getOriginalFilename());
                     String targetFilePath = fileUploadUtil.getSaveFilePath(file.getOriginalFilename(),resourceFileModel.getId(),0);
                     BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(targetFilePath)));
                     stream.write(bytes);
                     stream.close();
                     String fileUrl = fileUploadUtil.getFileUrl(file.getOriginalFilename(),resourceFileModel.getId(),0);
                     resourceFileService.update(resourceFileModel.getId(),file.getOriginalFilename(),fileUrl,targetFilePath,file.getSize());
                     restResultModel.setResult(200);
                 }else{
                     restResultModel.setResult(501);
                     restResultModel.setResult_msg("活动不存在");
                 }

            } catch (Exception e) {
                log.error("",e);
                restResultModel.setResult(500);
                restResultModel.setResult_msg("发生了不可预知的错误");
            }
        } else {
            restResultModel.setResult(404);
            restResultModel.setResult_msg("上传的文件不存在");
        }

        return restResultModel;

    }


    @RequestMapping(value = "/uploadSmall", method = RequestMethod.POST)
    public RestResultModel uploadSmall(@RequestParam("file") MultipartFile file,String resourceId) throws FileUploadException {
        RestResultModel restResultModel = new RestResultModel();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String targetFilePath = fileUploadUtil.getSaveFilePath(file.getOriginalFilename(),resourceId,1);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(targetFilePath)));
                stream.write(bytes);
                stream.close();
                String fileUrl = fileUploadUtil.getFileUrl(file.getOriginalFilename(),resourceId,1);
                ResourceFile resourceFileModel = resourceFileService.updateSmallFile(resourceId,fileUrl,file.getSize());
                restResultModel.setResult(200);
            } catch (Exception e) {
                log.error("",e);
                restResultModel.setResult(500);
                restResultModel.setResult_msg("发生了不可预知的错误");
            }
        } else {
            restResultModel.setResult(404);
            restResultModel.setResult_msg("上传的文件不存在");
        }
        return restResultModel;
    }

    @RequestMapping(value = "/delResource", method = RequestMethod.GET)
    public RestResultModel delResource(String id) throws FileUploadException {
        RestResultModel restResultModel = new RestResultModel();
        resourceFileService.del(id);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/selectResource", method = RequestMethod.GET)
    public RestResultModel selectResource(String partyId , String resourceId , Integer fileType) throws FileUploadException {
        RestResultModel restResultModel = new RestResultModel();
        bmsPartyResourceService.selectPartyResource(partyId,resourceId,fileType);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/delPartyResource", method = RequestMethod.GET)
    public RestResultModel delPartyResource(String partyId,String resourceId) {
        RestResultModel restResultModel = new RestResultModel();
        partyResourceService.delByPartyIdAndResourceId(partyId,resourceId);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/resourceFileList", method = RequestMethod.GET)
    public RestResultModel resourceFileList(String partyId,Integer fileType) {
        RestResultModel restResultModel = new RestResultModel();
        List<ResourceFile> resourceFileModelList = resourceFileService.findByPartyIdAndfileType(partyId,fileType);
        restResultModel.setResult(200);
        restResultModel.setData(resourceFileModelList);
        return restResultModel;
    }

    @RequestMapping(value = "/updateResourceName", method = RequestMethod.GET)
    public RestResultModel updateResourceName(String resourceId,String resourceName) {
        RestResultModel restResultModel = new RestResultModel();
        resourceFileService.updateResourceName(resourceId,resourceName);
        restResultModel.setResult(200);
        return restResultModel;
    }

}
