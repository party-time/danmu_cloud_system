package cn.partytime.model.param;

/**
 * Created by administrator on 2017/3/23.
 */
public class ObjectParamJson {

    private String paramId;

    private String paramValueId;

    private String paramName;

    private String des;

    private String paramValue;

    //0数字 1布尔值 2字符串 3数组 4其他paramTemplate
    private Integer valueType;

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getParamValueId() {
        return paramValueId;
    }

    public void setParamValueId(String paramValueId) {
        this.paramValueId = paramValueId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public Integer getValueType() {
        return valueType;
    }

    public void setValueType(Integer valueType) {
        this.valueType = valueType;
    }
}
