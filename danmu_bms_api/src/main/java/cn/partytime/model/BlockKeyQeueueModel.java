package cn.partytime.model;

import java.io.Serializable;

/**
 * Created by task on 16/6/22.
 */

public class BlockKeyQeueueModel implements Serializable {
    private static final long serialVersionUID = -6466022441824009888L;
    /**
     * 弹幕编号
     */
    private String id;
    /**
     * 弹幕内容
     */
    private String word;
    /**
     * 0:插入；1:更新；2:删除
     */
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
