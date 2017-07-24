package cn.partytime.model.projector;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by dm on 2017/6/14.
 */

@Document(collection = "projector")
public class Projector extends BaseModel {

    private String id;

    /**
     * 注册码
     */
    private String registerCode;


    private Date startTime;


    private long usedTime;


    private int realUsedHours;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getRealUsedHours() {
        return realUsedHours;
    }

    public void setRealUsedHours(int realUsedHours) {
        this.realUsedHours = realUsedHours;
    }

    public String getRegisterCode() {
        return registerCode;
    }

    public void setRegisterCode(String registerCode) {
        this.registerCode = registerCode;
    }

    public long getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(long usedTime) {
        this.usedTime = usedTime;
    }
}
