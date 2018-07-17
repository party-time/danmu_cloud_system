package cn.partytime.model.wechat;

import cn.partytime.baseModel.BaseModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "wechatUserWeekCount")
public class WechatUserWeekCount extends BaseModel {


    private String id;

    private String addressId;

    private Date startDate;

    private Date endDate;

    private int count;



}
