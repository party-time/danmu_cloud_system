package cn.partytime.model.wechatMsgTmpl;

import java.util.Map;

/**
 * Created by administrator on 2017/6/14.
 */
public class MsgTmpl {

    private String touser;

    private String template_id;

    private String url;

    private String topcolor;

    private Map<String,ValueTmpl> data;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTopcolor() {
        return topcolor;
    }

    public void setTopcolor(String topcolor) {
        this.topcolor = topcolor;
    }

    public Map<String, ValueTmpl> getData() {
        return data;
    }

    public void setData(Map<String, ValueTmpl> data) {
        this.data = data;
    }
}
