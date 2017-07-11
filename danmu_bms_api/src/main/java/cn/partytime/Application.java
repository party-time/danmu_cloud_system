

package cn.partytime;


import cn.partytime.util.FileUploadUtil;
import cn.partytime.util.H5TempUtil;
import cn.partytime.util.PartyTimeConfig;
import cn.partytime.util.UploadFlashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@RefreshScope
public class Application extends SpringBootServletInitializer {


	@Autowired
	private FileUploadUtil fileUploadUtil;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}


	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
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
