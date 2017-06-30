package cn.partytime.collector.listener;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MessageListenerConfig {

    @Resource(name = "realTimeDanmuListener")
    private RealTimeDanmuListener realTimeDanmuListener;

    @Resource(name = "partyCommandListener")
    private PartyCommandListener partyCommandListener;

    @Resource(name = "clientCommandListener")
    private ClientCommandListener clientCommandListener;

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(realTimeDanmuListenerAdapter(), new PatternTopic("addressId:danmu"));
        container.addMessageListener(partyCommandListenerAdapter(), new PatternTopic("party:command"));
        container.addMessageListener(clientCommandListenerAdapter(), new PatternTopic("client:command"));
        return container;
    }

    @Bean(name = "realTimeDanmuListenerAdapter")
    MessageListenerAdapter realTimeDanmuListenerAdapter() {
        return new MessageListenerAdapter(realTimeDanmuListener, "receiveMessage");
    }



    @Bean(name = "partyCommandListenerAdapter")
    MessageListenerAdapter partyCommandListenerAdapter() {
        return new MessageListenerAdapter(partyCommandListener, "receiveMessage");
    }

    @Bean(name = "clientCommandListenerAdapter")
    MessageListenerAdapter clientCommandListenerAdapter() {
        return new MessageListenerAdapter(clientCommandListener, "receiveMessage");
    }



}
