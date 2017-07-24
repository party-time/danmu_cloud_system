package cn.partytime.model.cms;

import java.util.List;

/**
 * Created by administrator on 2017/6/29.
 */
public class ColumnObject<T> {

    private Column column;

    private List<T> objectList;

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public List<T> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<T> objectList) {
        this.objectList = objectList;
    }
}
