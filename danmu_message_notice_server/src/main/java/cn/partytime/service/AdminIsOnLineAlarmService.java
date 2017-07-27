package cn.partytime.service;

import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.service.MessageService;
import org.springframework.stereotype.Service;

/**
 * Created by dm on 2017/7/19.
 */

@Service
public class AdminIsOnLineAlarmService  implements MessageService {
    @Override
    public void before(MessageObject messageObject) {

    }

    @Override
    public void after(MessageObject messageObject) {

    }
}
