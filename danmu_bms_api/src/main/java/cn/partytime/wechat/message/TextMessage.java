package cn.partytime.wechat.message;

/**
 * Created by Administrator on 2014/12/21.
 */
public class TextMessage extends BaseMessage{
    // 回复的消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
