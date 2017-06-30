package cn.partytime.model.manager;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

/**
 * Created by administrator on 2016/11/30.
 */
@Document(collection = "movie_schedule")
public class MovieSchedule extends BaseModel {

    @Field("_id")
    private String id;

    /**
     * 活动id
     */
    private String partyId;

    /**
     * 场地id
     */
    private String addressId;

    /**
     *排片的开始时间
     */
    private Date startTime;

    /**
     * 电影开始时间
     */
    private Date moviceStartTime;
    /**
     *排片的结束时间
     */
    private Date endTime;

    /**
     * 场次
     */
    private Integer number;


    /**
     * 客户端时间
     */
    private Long clientStartTime;



    private Long clientMoviceStartTime;

    private Long clientEndTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getMoviceStartTime() {
        return moviceStartTime;
    }

    public void setMoviceStartTime(Date moviceStartTime) {
        this.moviceStartTime = moviceStartTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getClientStartTime() {
        return clientStartTime;
    }

    public void setClientStartTime(Long clientStartTime) {
        this.clientStartTime = clientStartTime;
    }

    public Long getClientMoviceStartTime() {
        return clientMoviceStartTime;
    }

    public void setClientMoviceStartTime(Long clientMoviceStartTime) {
        this.clientMoviceStartTime = clientMoviceStartTime;
    }

    public Long getClientEndTime() {
        return clientEndTime;
    }

    public void setClientEndTime(Long clientEndTime) {
        this.clientEndTime = clientEndTime;
    }
}
