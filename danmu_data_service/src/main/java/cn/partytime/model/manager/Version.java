package cn.partytime.model.manager;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.StringUtils;

/**
 * Created by administrator on 2017/2/13.
 */
@Document(collection = "client_version")
public class Version extends BaseModel implements Comparable<Version> {


    private String id;

    private String name;

    private String version;

    private String describe;

    private String filePath;

    //类型 0 java  1 flash
    private Integer type;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public int compareTo(Version version) {
        String v1 = this.getVersion();
        String v2 = version.getVersion();
        if(!StringUtils.isEmpty(v1) && !StringUtils.isEmpty(v2)){
            String[] vSplit1 = v1.split(".");
            String[] vSplit2 = v2.split(".");
            if( null != vSplit1 && null != vSplit2 && vSplit1.length ==3 && vSplit2.length ==3){
                   for(int i=0;i<3;i++){
                       int value1 = Integer.parseInt(vSplit1[i]);
                       int value2 = Integer.parseInt(vSplit2[i]);
                       if( value1 > value2){
                           return 1;
                       }else{
                           return -1;
                       }
                   }
                   return vSplit1.length - vSplit2.length;
            }
        }
        return 0;
    }
}
