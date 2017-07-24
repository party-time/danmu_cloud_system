package cn.partytime.model.param;

import cn.partytime.model.manager.Param;
import cn.partytime.model.manager.ParamValue;

import java.util.List;

/**
 * Created by administrator on 2017/2/28.
 */
public class ParamTempJson {

    private String paramTempId;

    private String paramTempName;

    private List<Param> paramList;

    private List<ParamValue> paramValueList;

    public String getParamTempId() {
        return paramTempId;
    }

    public void setParamTempId(String paramTempId) {
        this.paramTempId = paramTempId;
    }

    public String getParamTempName() {
        return paramTempName;
    }

    public void setParamTempName(String paramTempName) {
        this.paramTempName = paramTempName;
    }

    public List<Param> getParamList() {
        return paramList;
    }

    public void setParamList(List<Param> paramList) {
        this.paramList = paramList;
    }

    public List<ParamValue> getParamValueList() {
        return paramValueList;
    }

    public void setParamValueList(List<ParamValue> paramValueList) {
        this.paramValueList = paramValueList;
    }
}
