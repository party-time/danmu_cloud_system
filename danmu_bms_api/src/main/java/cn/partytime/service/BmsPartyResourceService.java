package cn.partytime.service;

import cn.partytime.common.constants.Const;
import cn.partytime.model.PartyResourceFileResult;
import cn.partytime.model.manager.PartyResource;
import cn.partytime.model.manager.ResourceFile;
import cn.partytime.util.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2016/11/21.
 */
@Slf4j
@Service
public class BmsPartyResourceService {

    @Autowired
    private PartyResourceService partyResourceService;

    @Autowired
    private ResourceFileService resourceFileService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    public PartyResourceFileResult findByPartyId(String partyId){
        List<PartyResource> partyResourceList = partyResourceService.findByPartyId(partyId);
        PartyResourceFileResult partyResourceFileResult = new PartyResourceFileResult();
        if( null != partyResourceList && partyResourceList.size() > 0){
            List<ResourceFile> resourceFileList = resourceFileService.findByPartyId(partyId);
            if( null != resourceFileList && resourceFileList.size() > 0){
                List<ResourceFile> expressionList = new ArrayList<>();
                List<ResourceFile> specialImageList = new ArrayList<>();
                List<ResourceFile> specialVideoList = new ArrayList<>();
                for(ResourceFile resourceFile : resourceFileList){
                    if(resourceFile.getFileType() == Const.RESOURCE_H5_BACKGROUND){
                        partyResourceFileResult.setH5BackgroundFile(resourceFile);
                    }
                    if(resourceFile.getFileType() == Const.RESOURCE_EXPRESSIONS){
                        expressionList.add(resourceFile);
                    }
                    if(resourceFile.getFileType() == Const.RESOURCE_SPECIAL_IMAGES){
                        specialImageList.add(resourceFile);
                    }
                    if(resourceFile.getFileType() == Const.RESOURCE_SPECIAL_VIDEOS){
                        specialVideoList.add(resourceFile);
                    }
                }
                partyResourceFileResult.setExpressions(expressionList);

                partyResourceFileResult.setSpecialImages(specialImageList);

                partyResourceFileResult.setSpecialVideos(specialVideoList);

            }
        }

        return partyResourceFileResult;
    }

    public void selectPartyResource(String partyId ,String resourceId , Integer fileType){
        if( fileType == Const.RESOURCE_H5_BACKGROUND){
            List<PartyResource> partyResourceList =partyResourceService.findByPartyIdAndFileType(partyId,fileType);
            if( null != partyResourceList && partyResourceList.size() > 0){
                for(PartyResource partyResource : partyResourceList){
                    partyResourceService.delById(partyResource.getId());
                }
            }
        }
        partyResourceService.save(partyId,resourceId,fileType);
    }

    /**
     * 下载单个活动的资源
     * @param partyId
     */
    public String downLoadPartyResource(String partyId){
        List<ResourceFile> resourceFileList = resourceFileService.findByPartyId(partyId);
        if( null != resourceFileList && resourceFileList.size() > 0){
            String fileDirStr = fileUploadUtil.getPartyResourcePath()+File.separator+partyId;

            for(ResourceFile resourceFile : resourceFileList){
                String fileTypePath = null;
                if(resourceFile.getFileType() == Const.RESOURCE_SPECIAL_VIDEOS){
                    fileTypePath = "specialVideos";
                }
                if(resourceFile.getFileType() == Const.RESOURCE_SPECIAL_IMAGES){
                    fileTypePath = "specialImages";
                }
                if(resourceFile.getFileType() == Const.RESOURCE_EXPRESSIONS){
                    fileTypePath = "expressions";
                }
                File file = new File(fileDirStr+File.separator+fileTypePath);
                if(!file.exists()){
                    file.mkdirs();
                    log.info(" mkdir "+fileDirStr);
                }
                String cpFileCmd = "cp "+resourceFile.getFilePath()+" "+fileDirStr+File.separator+fileTypePath+File.separator;
                execShell(cpFileCmd);
                log.info(cpFileCmd);
            }
            String zipCmdStr = "zip -rmj "+fileUploadUtil.getPartyResourcePath()+File.separator+partyId+".zip "+fileDirStr+"/*";
            execShell(zipCmdStr);
            log.info(zipCmdStr);
            File file = new File(fileDirStr);
            if(file.exists()){
                file.delete();
            }
            return "resourceDownload"+File.separator+partyId+".zip";

        }
        return null;
    }


    private String execShell(String shellString) {
        log.info(shellString);
        Process process = null;
        StringBuffer sb = new StringBuffer();
        try {
            String[] commands = { "/bin/sh", "-c", shellString };
            log.info("exec cmd:"+shellString);
            process = Runtime.getRuntime().exec(commands);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = input.readLine()) != null) {
                sb.append(line);
            }
            input.close();
            int exitValue = process.waitFor();
            if (0 != exitValue) {
                log.info("call sh failed. error code is :" + exitValue);
            } else {
                log.info("sh exec success");
            }
        } catch (Exception e) {
            log.error("", e);
        }

        return sb.toString();
    }

}
