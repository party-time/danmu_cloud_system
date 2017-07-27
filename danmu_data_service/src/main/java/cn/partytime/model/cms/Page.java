package cn.partytime.model.cms;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * Created by administrator on 2017/6/29.
 */
@Document(collection = "page")
public class Page {


    private String id;

    //页面标题
    private String title;

    //page页面的url
    private String url;

    //栏目组
    private List<String> columnIdList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getColumnIdList() {
        return columnIdList;
    }

    public void setColumnIdList(List<String> columnIdList) {
        this.columnIdList = columnIdList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
