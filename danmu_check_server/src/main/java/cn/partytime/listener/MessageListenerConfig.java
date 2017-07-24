package cn.partytime.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MessageListenerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListenerConfig.class);


    @Resource(name = "realTimeDanmuMessageListener")
    private RealTimeDanmuMessageListener realTimeDanmuMessageListener;

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(realTimeDanmuMessageListenerAdapter(), new PatternTopic("partyId:danmu"));
        return container;
    }

    @Bean(name = "realTimeDanmuMessageListenerAdapter")
    MessageListenerAdapter realTimeDanmuMessageListenerAdapter() {
        return new MessageListenerAdapter(realTimeDanmuMessageListener, "receiveMessage");
    }

}
