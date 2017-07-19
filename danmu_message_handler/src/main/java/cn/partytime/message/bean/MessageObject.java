package cn.partytime.message.bean;

import java.util.Map;

/**
 * Created by dm on 2017/6/29.
 */
public class MessageObject<T> {



    /**
     * 日志编号
     */
    private String code;

    //系统信息中存放 key ip  key:name
    private Map<String,Object> systemInfo;

    /**阈值*/
    private int threshold=-1;

    /**当前值*/
    private int value=-1;

    /**自定义参数*/
    private T data;


    public MessageObject(String code,T data) {
        this.code = code;
        this.data = data;
    }

    public Map<String, Object> getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(Map<String, Object> systemInfo) {

        this.systemInfo = systemInfo;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
