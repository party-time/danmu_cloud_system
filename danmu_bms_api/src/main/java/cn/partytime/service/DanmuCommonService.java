package cn.partytime.service;

import cn.partytime.common.util.BooleanUtils;
import cn.partytime.common.util.IntegerUtils;
import cn.partytime.model.manager.ResourceFile;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dm on 2017/5/23.
 */



@Service
public class DanmuCommonService {


    private static final Logger logger = LoggerFactory.getLogger(BmsDanmuService.class);


    @Autowired
    private ResourceFileService resourceFileService;




    /**
     *
     * @param id
     * @param type
     * @return
     */
    public String getResourceContent(String id,int type){

        logger.info("查找资源:资源编号:{},资源类型:{}",id,type);

        if(type==1){
            //表情
            ResourceFile resourceFile = resourceFileService.findById(id);
            if(resourceFile==null){
                return "";
            }
            return resourceFile.getSmallFileUrl();
        }else if(type==2){
            //图片
            ResourceFile resourceFile = resourceFileService.findById(id);
            if(resourceFile==null){
                return "";
            }
            return resourceFile.getFileUrl();
        }else if(type==3){
            //视频
            ResourceFile resourceFile = resourceFileService.findById(id);
            if(resourceFile==null){
                return "";
            }
            return resourceFile.getResourceName();
        }else{
            //内容
            return id;
        }

    }

    /**
     * @param componentId 0无组件 1表情特效 2图片
     * @return
     */
    public Integer getDanmuType(String componentId){
        if("1".equals(componentId)){
            return 1;//表情特效
        }else if("2".equals(componentId)){
            return 2;//图片
        }else if("3".equals(componentId)) {
            return 3;//视频特效
        }else{
            return -1;
        }
    }


    /**
     *  @param componentId 0无组件 1表情特效 2图片
     * @return
     */
    public boolean checkSpecialComponent(String componentId){
        if("0".equals(componentId) || "1".equals(componentId) || "2".equals(componentId)){
            return true;
        }
        return  false;
    }



    public Object setShowNotArrayContent(String message,String componentId,int type){

        if(type==1){
            return BooleanUtils.objectConvertToBoolean(message);
        }
        return getResourceContent(message,getDanmuType(componentId));


    }

    public Object setShowNotArrayContent(String message,int type){
        if(type==1){
            return BooleanUtils.objectConvertToBoolean(message);
        }
        return message;
    }



    public List<String> setShowArrayContent(int componentType,String message,String componentId,String defaultValue){
        logger.info("componentType:{},message:{},defaultValue:{}",componentType,message,defaultValue);
        List<String> contentList = new ArrayList<String>();
        if(message==null){
            if(!StringUtils.isEmpty(defaultValue)){
                String array[] = defaultValue.split(",");
                for(String str:array){
                    contentList.add(str);
                }
                return contentList;
            }
            return contentList;
        }
        //组件的类型 0text 1textarea 2select  3radiobutton 4checkbox
        if(componentType==1){
            //如果是textArea机型字符串截取
            String array[] = message.split(",");
            if(array!=null && array.length>0){
                for(String str:array){
                    contentList.add(getResourceContent(str,getDanmuType(componentId)));
                }
            }
        }else{
            String array[] = message.split(",");
            if(array!=null && array.length>0){
                for(String str:array){
                    contentList.add(getResourceContent(str,getDanmuType(componentId)));
                }
            }
        }
        return contentList;
    }

    public List<String> setProtocolArrayContent(int componentType,String message,String defaultValue){
        logger.info("componentType:{},message:{},defaultValue:{}",componentType,message,defaultValue);
        List<String> contentList = new ArrayList<String>();
        if(message==null){
            String array[] = defaultValue.split(",");
            for(String str:array){
                contentList.add(str);
            }
            return contentList;
        }
        //组件的类型 0text 1textarea 2select  3radiobutton 4checkbox
        if(componentType==1){
            //如果是textArea机型字符串截取
            String array[] = message.split(",");
            if(array!=null && array.length>0){
                for(String str:array){
                    contentList.add(str);
                }
            }
        }else{
            String array[] = message.split(",");
            if(array!=null && array.length>0){
                for(String str:array){
                    contentList.add(str);
                }
            }
        }
        return contentList;
    }


}
