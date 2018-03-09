package cn.partytime.service;

import cn.partytime.common.constants.Const;
import cn.partytime.model.manager.Party;
import cn.partytime.model.manager.PartyResource;
import cn.partytime.model.manager.ResourceFile;
import cn.partytime.repository.manager.ResourceFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuwei on 2016/8/19.
 */
@Service
@Slf4j
public class ResourceFileService {

    @Autowired
    private ResourceFileRepository resourceFileRepository;

    @Autowired
    private PartyResourceService partyResourceService;

    @Autowired
    private PartyService partyService;

    @Resource(name = "danmuMongoTemplate")
    private MongoTemplate danmuMongoTemplate;


    public List<ResourceFile> findAll(){
        return resourceFileRepository.findAll();
    }
    public void save(ResourceFile resourceFile){
        resourceFileRepository.save(resourceFile);
    }

    public ResourceFile findById(String id){
        return  resourceFileRepository.findById(id);
    }

    public List<ResourceFile> findByIds(List<String> idList){
        return resourceFileRepository.findByIdIn(idList);
    }

    /**
     * 更新一个资源文件
     */
    public ResourceFile update(String id , String resourceName ,String fileUrl, String filePath , Long fileSize ){
        ResourceFile resourceFileModel = findById(id);
        resourceFileModel.setFileUrl(fileUrl);
        resourceFileModel.setFilePath(filePath);
        resourceFileModel.setFileSize(fileSize);
        resourceFileModel.setResourceName(resourceName);
        return resourceFileRepository.save(resourceFileModel);
    }

    public Integer getFileType(String fileName){
        if( fileName.indexOf(Const.H5_HEAD_NAME) != -1){
            return Const.RESOURCE_H5_BACKGROUND;
        }
        if( fileName.indexOf(Const.EXPRESSIONS_HEAD_NAME) != -1){
            return Const.RESOURCE_EXPRESSIONS;
        }
        if( fileName.indexOf(Const.SPECIAL_IMAGES_HEAD_NAME) != -1){
            return Const.RESOURCE_SPECIAL_IMAGES;
        }
        if( fileName.indexOf(Const.SPECIAL_VIDEOS_HEAD_NAME) != -1){
            return Const.RESOURCE_SPECIAL_VIDEOS;
        }
        if ( fileName.indexOf(Const.EXPRESSIONS_CONSTANT_HEAD_NAME) != -1){
            return Const.RESOURCE_EXPRESSIONS_CONSTANT;
        }
        return null;
    }

    public ResourceFile save(String partyId,String fileName){
        ResourceFile resourceFileModel = new ResourceFile();
        resourceFileModel.setOriginalName(fileName);
        resourceFileModel.setFileType(getFileType(fileName));
        resourceFileModel = resourceFileRepository.save(resourceFileModel);
        partyResourceService.save(partyId,resourceFileModel.getId(),resourceFileModel.getFileType());
        return resourceFileModel;
    }

    /**
     *
     * @param fileName
     * @return
     */
    public ResourceFile save(String fileName){
        ResourceFile resourceFileModel = new ResourceFile();
        resourceFileModel.setOriginalName(fileName);
        resourceFileModel.setFileType(Const.RESOURCE_SHOP_IMG);
        resourceFileModel = resourceFileRepository.save(resourceFileModel);
        return resourceFileModel;
    }

    public ResourceFile updateSmallFile(String id, String smallFileUrl,Long smallFileSize){
        ResourceFile resourceFile = resourceFileRepository.findOne(id);
        if( null != resourceFile){
            resourceFile.setSmallFileUrl(smallFileUrl);
            resourceFile.setSmallFileSize(smallFileSize);
            resourceFileRepository.save(resourceFile);
            return resourceFile;
        }else{
            return null;
        }

    }

    /**
     * 删除一个资源文件
     * @param id
     */
    public void del(String id){
        ResourceFile resourceFileModel = resourceFileRepository.findOne(id);
        if( null != resourceFileModel && !StringUtils.isEmpty(resourceFileModel.getFilePath())){
            File file = new File(resourceFileModel.getFilePath());
            if(file.exists()){
                file.delete();
            }
            //如果是表情需要删除掉小表情
            if(resourceFileModel.getFileType() == 1){
                File smallfile = new File(resourceFileModel.getFilePath().replace(".big.",".small."));
                if(smallfile.exists()){
                    smallfile.delete();
                }
            }
        }
        resourceFileRepository.delete(id);
        partyResourceService.delByResourceId(id);
    }

    /**
     * 根据一个活动查找所有资源文件
     * @param partyId
     * @return
     */
    public List<ResourceFile> findByPartyId(String partyId){
        List<PartyResource> partyResourceList =partyResourceService.findByPartyId(partyId);
        if( null != partyResourceList){
            List<String> resourceIdList = new ArrayList<String>();
            for(PartyResource partyResource : partyResourceList){
                resourceIdList.add(partyResource.getResourceId());
            }
            return this.findByIds(resourceIdList);
        }
        return null;
    }

    public List<ResourceFile> findByPartyIdAndfileType(String partyId,Integer fileType){
        Map<String,Object> map = this.findResourceMapByPartyId(partyId);
        if(fileType == Const.RESOURCE_H5_BACKGROUND ){
            return (List<ResourceFile>)map.get("h5Background");
        }else if(fileType == Const.RESOURCE_EXPRESSIONS){
            return (List<ResourceFile>)map.get("expressions");
        }else if(fileType == Const.RESOURCE_SPECIAL_IMAGES){
            return (List<ResourceFile>)map.get("specialImages");
        }else if(fileType == Const.RESOURCE_SPECIAL_VIDEOS){
            return (List<ResourceFile>)map.get("specialVideos");
        }else if(fileType == Const.RESOURCE_EXPRESSIONS_CONSTANT){
            return (List<ResourceFile>)map.get("expressionconstant");
        }
        return null;
    }

    /**
     * 根据一个活动分类查找资源图片
     * @param partyId
     * @return
     */
    public Map<String,Object> findResourceMapByPartyId(String partyId){
        Map<String,Object> resourceMap = new HashMap<String,Object>();
        List<ResourceFile> resourceFileModelList = findByPartyId(partyId);
        if( null != resourceFileModelList && resourceFileModelList.size() > 0){
            List<ResourceFile> bigExpressions = new ArrayList<ResourceFile>();
            List<ResourceFile> specialImages = new ArrayList<ResourceFile>();
            List<ResourceFile> specialVideos = new ArrayList<ResourceFile>();
            List<ResourceFile> h5Background = new ArrayList<ResourceFile>();
            List<ResourceFile> bigExpressionsConstant = new ArrayList<>();
            for(ResourceFile resourceFileModel : resourceFileModelList){
                if(Const.RESOURCE_EXPRESSIONS == resourceFileModel.getFileType()) {
                    bigExpressions.add(resourceFileModel);
                }else if( Const.RESOURCE_SPECIAL_IMAGES == resourceFileModel.getFileType()){
                    specialImages.add(resourceFileModel);
                }else if( Const.RESOURCE_SPECIAL_VIDEOS == resourceFileModel.getFileType()){
                    specialVideos.add(resourceFileModel);
                }else if (Const.RESOURCE_H5_BACKGROUND == resourceFileModel.getFileType()){
                    h5Background.add(resourceFileModel);
                }else if(Const.RESOURCE_EXPRESSIONS_CONSTANT == resourceFileModel.getFileType()){
                    bigExpressionsConstant.add(resourceFileModel);
                }
            }
            resourceMap.put("expressions",bigExpressions);
            resourceMap.put("specialImages",specialImages);
            resourceMap.put("specialVideos",specialVideos);
            resourceMap.put("h5Background",h5Background);
            resourceMap.put("expressionconstant",bigExpressionsConstant);
        }
        return resourceMap;
    }



    public Page<ResourceFile> findAllByType(int pageNo , int pageSize,int fileType){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(pageNo, pageSize, sort);
        return resourceFileRepository.findByFileType(fileType,pageRequest);
    }

    public Page<ResourceFile> findAllByType(int pageNo , int pageSize,int fileType,String resourceName){
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        PageRequest pageRequest = new PageRequest(pageNo, pageSize, sort);
        return resourceFileRepository.findByFileTypeAndResourceNameLike(fileType,resourceName,pageRequest);
    }

    public Map<String, Object> findByPartyShortName(String shortName){
        Party party = partyService.findByShortName(shortName);
        Map<String,Object> map = new HashMap<>();
        if( null != party){
            map= this.findResourceMapByPartyId(party.getId());
        }
        return map;
    }

    public Map<String,List<ResourceFile>> findByPartyIds(List<String> ids){
        if( null != ids){
            Map<String,List<ResourceFile>> map = new HashMap<>();
            for(String partyId : ids){

                map.put(partyId,this.findByPartyId(partyId));
            }
            return map;
        }
        return null;
    }

    public void updateResourceName(String id,String name){
        ResourceFile resourceFile = this.findById(id);
        if( null != resourceFile){
            resourceFile.setResourceName(name);
            resourceFileRepository.save(resourceFile);
        }
    }

    /**
     * 使用资源
     * @param resourceId
     */
    public void used(String resourceId){
        Query query = new Query(Criteria.where("_id").is(resourceId));
        Update update = new Update().inc("useTimes", 1);
        danmuMongoTemplate.updateFirst(query,update,"useTimes");
    }

    /**
     * 不使用资源
     * @param resourceId
     */
    public void unUsed(String resourceId){
        Query query = new Query(Criteria.where("_id").is(resourceId));
        Update update = new Update().inc("useTimes", -1);
        danmuMongoTemplate.updateFirst(query,update,"useTimes");
    }

    public void saveExpressionConstant(String partyId){
        List<ResourceFile> resourceFiles = resourceFileRepository.findByFileType(Const.RESOURCE_EXPRESSIONS_CONSTANT);
        if( null != resourceFiles){
            for( ResourceFile resourceFile : resourceFiles){
                partyResourceService.save(partyId,resourceFile.getId(),Const.RESOURCE_EXPRESSIONS_CONSTANT);
            }
        }
    }

    /**
     * 删除电影下的表情和特效图片
     * @param partyId
     */
    public void delResourceFileByPartyId(String partyId){
        List<ResourceFile> resourceFileList = this.findByPartyId(partyId);
        if( null != resourceFileList){
            for(ResourceFile resourceFile : resourceFileList){
                if( Const.RESOURCE_EXPRESSIONS == resourceFile.getFileType() ||
                       Const.RESOURCE_SPECIAL_IMAGES ==  resourceFile.getFileType()){
                    File file = new File(resourceFile.getFilePath());
                    if(file.exists()){
                        file.delete();
                    }
                    //如果是表情需要删除掉小表情
                    if(resourceFile.getFileType() == 1 ){
                        File smallfile = new File(resourceFile.getFilePath().replace(".big.",".small."));
                        if(smallfile.exists()){
                            smallfile.delete();
                        }
                    }
                }
            }
        }
    }

    /**
     * 删除所有已经下线的电影的表情和特效图片
     */
    public void delOffLineResourceFile(){
        List<Party> partyList = partyService.findByTypeAndStatus(1,4);
        if( null != partyList){
            for( Party party : partyList){
                delResourceFileByPartyId(party.getId());
            }

        }

    }


}
