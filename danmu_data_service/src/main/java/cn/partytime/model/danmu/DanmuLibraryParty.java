package cn.partytime.model.danmu;

import cn.partytime.baseModel.BaseModel;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by liuwei on 2016/10/21.
 */

@Data
public class DanmuLibraryParty  extends BaseModel {

    private String id;

    private String partyId;

    private String danmuLibraryId;

    private int densitry;
}
