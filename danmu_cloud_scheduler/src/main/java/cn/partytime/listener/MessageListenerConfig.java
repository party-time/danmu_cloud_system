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

@Component
public class MessageListenerConfig {

    private static final Logger logger = LoggerFactory.getLogger(MessageListenerConfig.class);


    @Autowired
    private CommandListener messageListener;

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(commandListenerAdapter(), new PatternTopic("secheduler:command:key"));
        return container;
    }

    @Bean(name = "commandListenerAdapter")
    MessageListenerAdapter commandListenerAdapter() {
        return new MessageListenerAdapter(messageListener, "receiveMessage");
    }
}
