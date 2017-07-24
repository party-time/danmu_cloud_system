package cn.partytime.util;


import cn.partytime.common.constants.Const;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Created by liuwei on 2016/8/19.
 */

@ConfigurationProperties(prefix = "fileUpload")
public class FileUploadUtil {

    //当上传文件超过限制时设定的临时文件位置，注意是绝对路径
    private String tempPath;

    //文件上传目标目录，注意是绝对路径
    private String dstPath;

    //设置允许用户上传文件大小,单位:字节)
    private String maxSize;

    //H5背景图保存路径
    private String h5BackgroundPath;

    //表情包大图保存路径
    private String expressionsPath;

    //特效图片保存路径
    private String specialImagesPath;

    //特效视频保存路径
    private String specialVideosPath;

    //商店图片保存路径
    private String shopImagePath;

    //文件访问地址
    private String url;

    private String h5FileName = "h5Background";

    private String screenSavePath;


    public String checkFile(MultipartFile file){
        if( null == file || file.isEmpty()){
            return "文件不存在";
        }
        String fileName = file.getOriginalFilename();
        if( fileName.indexOf(Const.H5_HEAD_NAME) ==-1 || fileName.indexOf(Const.EXPRESSIONS_HEAD_NAME) == -1
                || fileName.indexOf(Const.SPECIAL_IMAGES_HEAD_NAME) == -1 || fileName.indexOf(Const.SPECIAL_VIDEOS_HEAD_NAME) == -1){

            return "文件命名格式不正确";
        }

        return null;
    }



    public String getSaveFilePath(String fileName,String id,Integer smallOrBig){
        String ext = "."+this.getFileExt(fileName);

        String path = "";
        File file = null;
        if( fileName.indexOf(Const.H5_HEAD_NAME) != -1){
            file = new File(this.dstPath+ File.separator+this.h5BackgroundPath);
            path = this.dstPath+ File.separator + this.h5BackgroundPath+File.separator+id+ext;
        }
        if( 1 == smallOrBig){
            ext = ".small"+ext;
            path = this.dstPath+ File.separator+ this.expressionsPath+File.separator+id+ext;
        }
        if( fileName.indexOf(Const.EXPRESSIONS_HEAD_NAME) != -1 && (null== smallOrBig || 0==smallOrBig)){
            file = new File(this.dstPath+ File.separator+this.expressionsPath);
            ext = ".big"+ext;
            path = this.dstPath+ File.separator+ this.expressionsPath+File.separator+id+ext;
        }

        if( fileName.indexOf(Const.SPECIAL_IMAGES_HEAD_NAME) != -1){
            file = new File(this.dstPath+ File.separator+this.specialImagesPath);
            path = this.dstPath+ File.separator+ this.specialImagesPath+File.separator+id+ext;
        }
        if( fileName.indexOf(Const.SPECIAL_VIDEOS_HEAD_NAME) != -1){
            file = new File(this.dstPath+ File.separator+this.specialVideosPath);
            path = this.dstPath+ File.separator+ this.specialVideosPath+File.separator+id+ext;
        }
        if (null != file && !file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public String getFileUrl(String fileName,String id,Integer smallOrBig){
        String ext = "."+this.getFileExt(fileName);

        String url = "";
        if( fileName.indexOf(Const.H5_HEAD_NAME) != -1){
            url =  "/" + this.h5BackgroundPath + "/"+id+ext;
        }
        if( 1 == smallOrBig){
            ext = ".small"+ext;
            url = "/"+ this.expressionsPath+"/"+id+ext;
        }
        if( fileName.indexOf(Const.EXPRESSIONS_HEAD_NAME) != -1 && ( null== smallOrBig || 0==smallOrBig)){
            ext = ".big"+ext;
            url = "/"+ this.expressionsPath+"/"+id+ext;
        }
        if( fileName.indexOf(Const.SPECIAL_IMAGES_HEAD_NAME) != -1){
            url = "/"+ this.specialImagesPath+"/"+id+ext;
        }
        if( fileName.indexOf(Const.SPECIAL_VIDEOS_HEAD_NAME) != -1){
            url = "/"+ this.specialVideosPath+"/"+id+ext;
        }

        return url;
    }

    public String getSaveShopImageUrl(String id,String fileName){
        String ext = "."+this.getFileExt(fileName);
        File file = new File(this.dstPath+ File.separator+this.shopImagePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String path = this.dstPath+ File.separator+ this.shopImagePath+File.separator+id+ext;
        return path;
    }

    public String getFileUrl(String fileName,String id){
        String ext = "."+this.getFileExt(fileName);
        return "/"+ this.shopImagePath+"/"+id+ext;
    }




    /**
     * 返回活动的基础路径
     *
     * @param partyShortName
     * @return
     */
    private String getBasePartyPath(String partyShortName) {
        return this.dstPath + File.separator + partyShortName + File.separator;
    }

    public String getSaveH5Path(String partyShortName, String name) {
        String ext = this.getFileExt(name);
        File file = new File(getBasePartyPath(partyShortName) + this.h5BackgroundPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return getBasePartyPath(partyShortName) + this.h5BackgroundPath + File.separator + this.h5FileName + ext;
    }



    public String getSaveSpecialImagesPath(String partyShortName) {
        String path = getBasePartyPath(partyShortName) + this.specialImagesPath;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public String getSaveSpecialVideosPath(String partyShortName) {
        String path = getBasePartyPath(partyShortName) + this.specialVideosPath;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    public String getFileExt(String name) {
        if (!StringUtils.isEmpty(name)) {
            if (name.indexOf(".") == -1) {
                return null;
            } else {
                String[] nameSubs = name.split("\\.");
                return nameSubs[nameSubs.length-1];
            }
        } else {
            return null;
        }
    }

    public String getH5BackgroundUrl(String partyShortName, String name) {
        String ext = this.getFileExt(name);
        return "/" + partyShortName + "/" + this.h5FileName + "/" + this.h5FileName + ext;
    }

    public String getSpecialImagesUrl(String partyShortName) {
        return "/" + partyShortName + "/" + this.specialImagesPath + "/";
    }

    public String getSpecialVideoUrl(String partyShortName) {
        return "/" + partyShortName + "/" + this.specialVideosPath + "/";
    }

    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public String getDstPath() {
        return dstPath;
    }

    public void setDstPath(String dstPath) {
        this.dstPath = dstPath;
    }

    public String getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(String maxSize) {
        this.maxSize = maxSize;
    }

    public String getH5BackgroundPath() {
        return h5BackgroundPath;
    }

    public void setH5BackgroundPath(String h5BackgroundPath) {
        this.h5BackgroundPath = h5BackgroundPath;
    }

    public String getExpressionsPath() {
        return expressionsPath;
    }

    public void setExpressionsPath(String expressionsPath) {
        this.expressionsPath = expressionsPath;
    }

    public String getSpecialImagesPath() {
        return specialImagesPath;
    }

    public void setSpecialImagesPath(String specialImagesPath) {
        this.specialImagesPath = specialImagesPath;
    }

    public String getSpecialVideosPath() {
        return specialVideosPath;
    }

    public void setSpecialVideosPath(String specialVideosPath) {
        this.specialVideosPath = specialVideosPath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScreenSavePath() {
        return screenSavePath;
    }

    public void setScreenSavePath(String screenSavePath) {
        this.screenSavePath = screenSavePath;
    }

    public String getShopImagePath() {
        return shopImagePath;
    }

    public void setShopImagePath(String shopImagePath) {
        this.shopImagePath = shopImagePath;
    }
}
