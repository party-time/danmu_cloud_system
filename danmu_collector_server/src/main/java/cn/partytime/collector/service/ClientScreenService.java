package cn.partytime.collector.service;

import cn.partytime.model.client.DanmuClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Created by lENOVO on 2016/9/1.
 */
@Service
public class ClientScreenService {



    /**
     * 获取最大屏幕编号
     * @param addressId
     * @return
     */
    public int getMaxScreenId(String addressId){
        int screenId = 0;
        synchronized (ClientScreenService.class){
            Page<DanmuClient> preDanmuModels = null;
            if (preDanmuModels != null && preDanmuModels.getSize() != 0) {
                //最大屏幕编号的客户端
                DanmuClient maxScreenIdDanmuClient = preDanmuModels.getContent().get(0);
                screenId = maxScreenIdDanmuClient.getScreenId()+1;
            }else {
                screenId =screenId+1;
            }
        }
        return screenId;
    }

}
