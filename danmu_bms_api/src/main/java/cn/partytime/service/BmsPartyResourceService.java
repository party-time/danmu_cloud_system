package cn.partytime.service;

import cn.partytime.common.constants.Const;
import cn.partytime.model.PartyResourceFileResult;
import cn.partytime.model.manager.PartyResource;
import cn.partytime.model.manager.ResourceFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2016/11/21.
 */
@Service
public class BmsPartyResourceService {

    @Autowired
    private PartyResourceService partyResourceService;

    @Autowired
    private ResourceFileService resourceFileService;

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

}
