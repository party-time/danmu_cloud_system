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


    @Resource(name = "partyDanmuListener")
    private PartyDanmuListener partyDanmuListener;

    @Resource(name = "filmDanmuListener")
    private FilmDanmuListener filmDanmuListener;



    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(partyDanmuListenerAdapter(), new PatternTopic("partyId:danmu"));
        container.addMessageListener(filmDanmuListenerAdapter(), new PatternTopic("filmId:danmu"));
        return container;
    }

    @Bean(name = "partyDanmuListenerAdapter")
    MessageListenerAdapter partyDanmuListenerAdapter() {
        return new MessageListenerAdapter(partyDanmuListener, "receiveMessage");
    }

    @Bean(name = "filmDanmuListenerAdapter")
    MessageListenerAdapter filmDanmuListenerAdapter() {
        return new MessageListenerAdapter(filmDanmuListener, "receiveMessage");
    }

}
