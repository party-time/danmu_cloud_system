package cn.partytime.model;

import java.util.List;
import java.util.Map;

/**
 * Created by liuwei on 2016/9/27.
 */
public class ResourceConfigJson {

    private Map<String,Object> params;

    private List<PartyResourceJson> partys;

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public List<PartyResourceJson> getPartys() {
        return partys;
    }

    public void setPartys(List<PartyResourceJson> partys) {
        this.partys = partys;
    }
}
