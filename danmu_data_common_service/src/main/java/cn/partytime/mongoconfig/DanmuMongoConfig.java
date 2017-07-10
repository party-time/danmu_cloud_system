package cn.partytime.mongoconfig;

import com.mongodb.MongoClient;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by liuwei on 16/6/15.
 */
@Configuration
@EnableMongoRepositories(basePackages = "cn.partytime.repository.danmu", mongoTemplateRef = "danmuMongoTemplate")
public class DanmuMongoConfig {

    @Primary
    @Bean(name = "danmuMongoProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.2")
    public MongoProperties danmuMongoProperties() {
        return new MongoProperties();
    }

    @Primary
    @Bean(name = "danmuMongoTemplate")
    public MongoTemplate danmuMongoTemplate() throws Exception {
        MongoProperties mongoProperties = danmuMongoProperties();
        MongoClient mongoClient = mongoProperties.createMongoClient(null);
        return new MongoTemplate(mongoClient, mongoProperties.getDatabase());
    }
}
