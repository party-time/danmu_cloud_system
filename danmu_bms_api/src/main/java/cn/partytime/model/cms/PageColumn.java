package cn.partytime.model.cms;

import java.util.List;

/**
 * Created by administrator on 2017/6/29.
 */
public class PageColumn {

    private Page page;

    private List<ColumnObject> columnObjectList;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<ColumnObject> getColumnObjectList() {
        return columnObjectList;
    }

    public void setColumnObjectList(List<ColumnObject> columnObjectList) {
        this.columnObjectList = columnObjectList;
    }
}
