package cn.partytime.mongoconfig;

import com.mongodb.MongoClient;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by liuwei on 16/6/15.
 */
@Configuration
@EnableMongoRepositories(basePackages = "cn.partytime.repository.user", mongoTemplateRef = "userMongoTemplate")
public class UserMongoConfig {

    @Bean(name = "userMongoProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.3")
    public MongoProperties userMongoProperties() {
        return new MongoProperties();
    }


    @Bean(name = "userMongoTemplate")
    public MongoTemplate userMongoTemplate() throws Exception {
        MongoProperties mongoProperties = userMongoProperties();
        MongoClient mongoClient = mongoProperties.createMongoClient(null);
        return new MongoTemplate(mongoClient, mongoProperties.getDatabase());
    }

}
