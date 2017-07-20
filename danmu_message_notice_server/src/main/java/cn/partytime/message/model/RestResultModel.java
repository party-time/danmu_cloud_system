package cn.partytime.message.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by jack on 15/6/26.
 */
@JsonSerialize
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResultModel {

  private int result;
  private String result_msg;
  private Object data;

  public int getResult() {
    return result;
  }

  public void setResult(int result) {
    this.result = result;
  }

  public String getResult_msg() {
    return result_msg;
  }

  public void setResult_msg(String result_msg) {
    this.result_msg = result_msg;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
