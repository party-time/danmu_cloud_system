package cn.partytime.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MessageListenerConfig {

    private static final Logger logger = LoggerFactory.getLogger(MessageListenerConfig.class);


    @Autowired
    private RedisMessageListener messageListener;

    @Resource(name = "alarmMessageListener")
    private AlarmMessageListener alarmMessageListener;

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(blockKeyListenerAdapter(), new PatternTopic("blockKey"));
        container.addMessageListener(alarmMessageListenerAdapter(), new PatternTopic("alarm:cache"));
        return container;
    }

    @Bean(name = "blockKeyListenerAdapter")
    MessageListenerAdapter blockKeyListenerAdapter() {
        return new MessageListenerAdapter(messageListener, "receiveMessage");
    }


    @Bean(name = "alarmMessageListenerAdapter")
    MessageListenerAdapter alarmMessageListenerAdapter() {
        return new MessageListenerAdapter(alarmMessageListener, "receiveMessage");
    }

}
