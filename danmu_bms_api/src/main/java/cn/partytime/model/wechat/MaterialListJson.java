package cn.partytime.model.wechat;

/**
 * Created by administrator on 2017/4/7.
 */
public class MaterialListJson {

    private String type;

    private Integer offset;

    private Integer count;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
