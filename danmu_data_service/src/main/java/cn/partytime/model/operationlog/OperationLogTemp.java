package cn.partytime.model.operationlog;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "operation_log_temp")
public class OperationLogTemp {

    private String id;

    //操作日志的标题
    private String title;

    //日志内容 有占位符需要替换掉
    private String content;

    //日志的英文标示
    private String key;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
