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
@EnableMongoRepositories(basePackages = "cn.partytime.repository.manager", mongoTemplateRef = "managerMongoTemplate")
public class DanmuManagerMongoConfig {

    @Bean(name = "managerMongoProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb.1")
    public MongoProperties managerMongoProperties() {
        return new MongoProperties();
    }


    @Bean(name = "managerMongoTemplate")
    public MongoTemplate managerMongoTemplate() throws Exception {
        MongoProperties mongoProperties = managerMongoProperties();
        MongoClient mongoClient = mongoProperties.createMongoClient(null);
        return new MongoTemplate(mongoClient, mongoProperties.getDatabase());
    }

}

