package cn.partytime.model;

/**
 * Created by liuwei on 2016/8/19.
 */


import lombok.Data;

/**
 * 资源文件
 */

@Data
public class ResourceFileModel {

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
        if (this.fileUrl!=null && this.fileUrl!="") {
            String[] ss = this.fileUrl.split("/");
            return ss[ss.length-1];
        }
        return "";
    }


}


