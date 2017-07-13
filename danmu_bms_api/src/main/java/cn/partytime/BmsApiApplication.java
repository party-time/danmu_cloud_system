

package cn.partytime;


import cn.partytime.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@RefreshScope
public class BmsApiApplication extends SpringBootServletInitializer {


	@Autowired
	private FileUploadUtil fileUploadUtil;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BmsApiApplication.class);
	}


	public static void main(String[] args) throws Exception {
		SpringApplication.run(BmsApiApplication.class, args);
	}

	@Bean
	public MultipartConfigElement multipartConfig() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize(fileUploadUtil.getMaxSize());
		factory.setMaxRequestSize(fileUploadUtil.getMaxSize());
		factory.setLocation(fileUploadUtil.getTempPath());
		return factory.createMultipartConfig();
	}


}
