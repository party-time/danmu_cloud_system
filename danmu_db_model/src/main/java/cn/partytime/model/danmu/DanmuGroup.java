package cn.partytime.model.danmu;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by task on 16/6/24.
 */
@Document(collection = "danmu_group")
public class DanmuGroup extends BasePool {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
