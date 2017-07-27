package cn.partytime.model.manager;

/**
 * Created by liuwei on 2016/8/19.
 */

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.StringUtils;

/**
 * 资源文件
 */
@Document(collection = "resource_file")
public class ResourceFile extends BaseModel{


    private String id;

    /**
     * 文件原始名称
     */
    private String originalName;

    /**
     *资源名称
     */
    private String resourceName;

    /**
     * 文件类型
     */
    private Integer fileType;

    /**
     * 文件下载地址
     */
    private String fileUrl;

    /**
     * 文件保存的服务器路径
     */
    private String filePath;

    /**
     * 本地的资源文件路径
     */
    private String localFilePath;

    /**
     * 压缩图片的路径
     */
    private String smallFileUrl;

    /**
     * 文件尺寸
     */
    private Long fileSize;

    /**
     * 缩略图文件尺寸
     */
    private Long smallFileSize;

    /**
     * 被使用的次数，初始值是0
     */
    private Integer useTimes=0;


    public String getName(){
        if (!StringUtils.isEmpty(this.fileUrl)) {
            String[] ss = this.fileUrl.split("/");
            return ss[ss.length-1];
        }
        return "";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getSmallFileUrl() {
        return smallFileUrl;
    }

    public void setSmallFileUrl(String smallFileUrl) {
        this.smallFileUrl = smallFileUrl;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getSmallFileSize() {
        return smallFileSize;
    }

    public void setSmallFileSize(Long smallFileSize) {
        this.smallFileSize = smallFileSize;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public Integer getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(Integer useTimes) {
        this.useTimes = useTimes;
    }
}


