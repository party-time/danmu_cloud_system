package cn.partytime.model.command;

/**
 * Created by lENOVO on 2016/11/24.
 */
public class PartyComModel {

    /**1:活动开始；2：电影开始；3：活动结束*/
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
