package cn.partytime.model.command;

/**
 * Created by lENOVO on 2016/11/24.
 */
public class DelayTimeComModel {

    /**true:增加,false:减少*/
    private boolean status;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
