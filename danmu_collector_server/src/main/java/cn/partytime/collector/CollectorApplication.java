

package cn.partytime.collector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("cn.partytime")
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class CollectorApplication {

    public static void main(String[] args) throws Exception {
        //SpringApplication.run(Application.class, args);
        SpringApplication app = new SpringApplication(CollectorApplication.class);
        app.setWebEnvironment(false);
        app.run(args);
    }

}











