package cn.partytime.message.service;

import cn.partytime.message.bean.MessageObject;

/**
 * Created by dm on 2017/6/29.
 */
public interface MessageService {

    public void before(MessageObject messageObject);

    public void after(MessageObject messageObject);
}
