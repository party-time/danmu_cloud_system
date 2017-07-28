package cn.partytime.service;

import cn.partytime.logicService.MessageLogicService;
import cn.partytime.message.bean.MessageObject;
import cn.partytime.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by dm on 2017/7/19.
 */

@Service
public class ProjectorCloseAlarmService implements MessageService {
    @Autowired
    private MessageLogicService messageLogicService;

    @Override
    public void before(MessageObject messageObject) {
        Map<String,Object> map = (Map<String,Object>)messageObject.getData();
    }

    @Override
    public void after(MessageObject messageObject) {
        messageLogicService.sendMessage(messageObject);
    }
}
