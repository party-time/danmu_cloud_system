package cn.partytime.check.model.command;

/**
 * 动画 特效
 * Created by lENOVO on 2016/11/24.
 */
public class VideoComModel {

    /**视频编号*/
    private String id;

    /**0:为开始 1:开始*/
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
