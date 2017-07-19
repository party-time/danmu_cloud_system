package cn.partytime.collector.service;

import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.service.MessageService;
import org.springframework.stereotype.Service;

/**
 * Created by dm on 2017/7/18.
 */

@Service
public class PreDanmuMessageService implements MessageService {

    @Override
    public void before(MessageObject messageObject) {

    }

    @Override
    public void after(MessageObject messageObject) {

    }
}
