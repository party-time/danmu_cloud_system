package cn.partytime;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;

@EnableEurekaServer
@SpringBootApplication
public class DanmuRegisterEurekaApplication {
    public static void main(String[] args) {
		new SpringApplicationBuilder(DanmuRegisterEurekaApplication.class).web(true).run(args);
	}
}