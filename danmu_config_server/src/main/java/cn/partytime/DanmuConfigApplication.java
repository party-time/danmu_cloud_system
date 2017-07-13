package cn.partytime;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableConfigServer
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class DanmuConfigApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(DanmuConfigApplication.class).web(true).run(args);
	}

}
