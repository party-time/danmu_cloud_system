package cn.partytime.controller.shop;

import cn.partytime.model.PageResultModel;
import cn.partytime.model.RestResultModel;
import cn.partytime.model.manager.ResourceFile;
import cn.partytime.model.shop.Item;
import cn.partytime.service.ResourceFileService;
import cn.partytime.service.shop.ItemService;
import cn.partytime.util.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by administrator on 2017/6/26.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/item")
@Slf4j
public class ItemConteoller {

    @Autowired
    private ItemService itemService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Autowired
    private ResourceFileService resourceFileService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public PageResultModel itemPage(Integer pageNumber , Integer pageSize ){
        pageNumber = pageNumber-1;
        Page<Item> itemPage = itemService.findAll(pageNumber,pageSize);
        return new PageResultModel(itemPage);
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResultModel saveItem(String content,String dmCmdId,String name,Integer showPrice,
                                    String title,Integer truePrice,Integer type,String url){
        RestResultModel restResultModel = new RestResultModel();
        Item item = new Item();
        item.setContent(content);
        item.setDmCmdId(dmCmdId);
        item.setName(name);
        item.setShowPrice(showPrice);
        item.setTitle(title);
        item.setTruePrice(truePrice);
        item.setType(type);
        item.setUrl(url);
        restResultModel.setResult(200);
        restResultModel.setData(itemService.save(item));
        return restResultModel;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public RestResultModel get(String id){
        RestResultModel restResultModel = new RestResultModel();
        Item item = itemService.findById(id);
        restResultModel.setResult(200);
        restResultModel.setData(item);
        return restResultModel;
    }

    @RequestMapping(value = "/del", method = RequestMethod.GET)
    public RestResultModel del(String id){
        RestResultModel restResultModel = new RestResultModel();
        Item item = itemService.findById(id);
        if( null != item){
            if( !StringUtils.isEmpty(item.getCoverImgId())){
                ResourceFile coverResourceFile = resourceFileService.findById(item.getCoverImgId());
                if( null != coverResourceFile){
                    File file1 = new File(coverResourceFile.getFilePath());
                    if(file1.exists()){
                        file1.delete();
                    }
                    resourceFileService.del(item.getCoverImgId());
                }
            }
            if( null != item.getImageIds()){
                for(String resourceId : item.getImageIds()){
                    ResourceFile resourceFile = resourceFileService.findById(resourceId);
                    if( null != resourceFile){
                        File file1 = new File(resourceFile.getFilePath());
                        if(file1.exists()){
                            file1.delete();
                        }
                        resourceFileService.del(resourceId);
                    }
                }

            }
        }
        itemService.delById(id);
        restResultModel.setResult(200);
        return restResultModel;
    }



    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public RestResultModel update(String id,String content,String dmCmdId,String name,Integer showPrice,
                                    String title,Integer truePrice,Integer type,String url){

        RestResultModel restResultModel = new RestResultModel();
        if(StringUtils.isEmpty(id)){
            restResultModel.setResult(404);
            restResultModel.setResult_msg("id不能为空");
            return restResultModel;
        }
        Item item = itemService.findById(id);
        if( null == item){
            restResultModel.setResult(404);
            restResultModel.setResult_msg("商品不存在");
            return restResultModel;
        }

        item.setContent(content);
        item.setDmCmdId(dmCmdId);
        item.setName(name);
        item.setShowPrice(showPrice);
        item.setTitle(title);
        item.setTruePrice(truePrice);
        item.setType(type);
        item.setUrl(url);
        itemService.update(item);
        restResultModel.setResult(200);
        return restResultModel;
    }

    @RequestMapping(value = "/uploadCoverImage", method = RequestMethod.POST)
    public RestResultModel uploadCoverImage(@RequestParam("file") MultipartFile file, String itemId) throws FileUploadException {
        RestResultModel restResultModel = new RestResultModel();
        if (!file.isEmpty()) {
            try {
                ResourceFile resourceFileModel = resourceFileService.save(file.getOriginalFilename());

                byte[] bytes = file.getBytes();
                String targetFilePath = fileUploadUtil.getSaveShopImageUrl(resourceFileModel.getId(),file.getOriginalFilename());
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(targetFilePath)));
                stream.write(bytes);
                stream.close();
                String fileUrl = fileUploadUtil.getFileUrl(file.getOriginalFilename(),resourceFileModel.getId());
                resourceFileService.update(resourceFileModel.getId(),file.getOriginalFilename(),fileUrl,targetFilePath,file.getSize());
                Item item = itemService.findById(itemId);
                if( !StringUtils.isEmpty(item.getCoverImgId())){
                    ResourceFile resourceFile = resourceFileService.findById(item.getCoverImgId());
                    if( null != resourceFile){
                        File file1 = new File(resourceFile.getFilePath());
                        if(file1.exists()){
                            file1.delete();
                        }
                    }
                    resourceFileService.del(item.getCoverImgId());
                }
                item.setCoverImgId(resourceFileModel.getId());
                itemService.update(item);
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

    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public RestResultModel uploadImage(@RequestParam("file") MultipartFile file, String itemId) throws FileUploadException {
        RestResultModel restResultModel = new RestResultModel();
        if (!file.isEmpty()) {
            try {
                ResourceFile resourceFileModel = resourceFileService.save(file.getOriginalFilename());

                byte[] bytes = file.getBytes();
                String targetFilePath = fileUploadUtil.getSaveShopImageUrl(resourceFileModel.getId(),file.getOriginalFilename());
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(targetFilePath)));
                stream.write(bytes);
                stream.close();
                String fileUrl = fileUploadUtil.getFileUrl(file.getOriginalFilename(),resourceFileModel.getId());
                resourceFileService.update(resourceFileModel.getId(),file.getOriginalFilename(),fileUrl,targetFilePath,file.getSize());
                Item item = itemService.findById(itemId);
                if( null == item.getImageIds()){
                    item.setImageIds(new ArrayList<>());
                }
                item.getImageIds().add(resourceFileModel.getId());
                itemService.update(item);
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


    @RequestMapping(value = "/findImg", method = RequestMethod.GET)
    public RestResultModel findImg(String id){
        RestResultModel restResultModel = new RestResultModel();
        Item item = itemService.findById(id);
        if( null == item){
            restResultModel.setResult(404);
            restResultModel.setResult_msg("商品不存在");
            return restResultModel;
        }
        Map<String,Object> fileMap = new HashMap<>();
        if(!StringUtils.isEmpty(item.getCoverImgId())){
            ResourceFile resourceFile = resourceFileService.findById(item.getCoverImgId());
            fileMap.put("cover",resourceFile);
        }
        if( null != item.getImageIds() && item.getImageIds().size() > 0){
            List<ResourceFile> resourceFileList = new ArrayList<>();
            for(String rId : item.getImageIds()){
                ResourceFile resourceFile = resourceFileService.findById(rId);
                resourceFileList.add(resourceFile);
            }
            fileMap.put("imageList",resourceFileList);

        }
        restResultModel.setResult(200);
        restResultModel.setData(fileMap);
        return restResultModel;
    }

    @RequestMapping(value = "/delImg", method = RequestMethod.GET)
    public RestResultModel delImg(String resourceId,String itemId) {
        RestResultModel restResultModel = new RestResultModel();
        ResourceFile resourceFile = resourceFileService.findById(resourceId);
        if( null != resourceFile){
            File file1 = new File(resourceFile.getFilePath());
            if(file1.exists()){
                file1.delete();
            }
            resourceFileService.del(resourceId);
        }
        Item item = itemService.findById(itemId);
        if(null != item && null !=item.getImageIds()){
            item.getImageIds().remove(resourceId);
            itemService.update(item);
        }
        restResultModel.setResult(200);
        return restResultModel;
    }





}
