package cn.partytime.model.welcome;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "wechat_welcome")
public class Welcome {

    private String id;

    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
