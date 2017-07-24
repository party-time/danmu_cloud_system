package cn.partytime.baseModel;

/**
 * Created by lENOVO on 2016/11/16.
 */
public class GropCountModel {

    /**
     * 分组的key
     */
    private long key;

    /**
     * 组内数量
     */
    private long count;

    public GropCountModel() {
    }

    public GropCountModel(long key, long count) {
        this.key = key;
        this.count = count;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
