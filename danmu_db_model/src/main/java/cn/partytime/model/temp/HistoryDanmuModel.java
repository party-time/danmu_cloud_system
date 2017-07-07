package cn.partytime.model.temp;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by jack on 15/6/29.
 */
@Document(collection = "intranet_danmaku")
public class HistoryDanmuModel {

  private String id;
  private String color;
  private String msg;
  private Boolean isBlocked = false;
  private Integer test = 0;//0普通弹幕；1测试弹幕
  private Date created = new Date();
  private Integer isPre = 0;//0普通弹幕；1预制弹幕；2带时间参数的预制弹幕
  private Integer preTime;//预制弹幕出现的时间

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Boolean getIsBlocked() {
    return isBlocked;
  }

  public void setIsBlocked(Boolean isBlocked) {
    this.isBlocked = isBlocked;
  }

  public Integer getTest() {
    return test;
  }

  public void setTest(Integer test) {
    this.test = test;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Integer getIsPre() {
    return isPre;
  }

  public void setIsPre(Integer isPre) {
    this.isPre = isPre;
  }

  public Integer getPreTime() {
    return preTime;
  }

  public void setPreTime(Integer preTime) {
    this.preTime = preTime;
  }
}
