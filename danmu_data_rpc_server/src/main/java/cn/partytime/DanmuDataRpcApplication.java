

package cn.partytime;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@EnableDiscoveryClient
@SpringBootApplication
@RefreshScope
public class DanmuDataRpcApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(DanmuDataRpcApplication.class, args);
	}

}
