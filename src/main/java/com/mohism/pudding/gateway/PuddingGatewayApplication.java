package com.mohism.pudding.gateway;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 网关服务
 *
 * @author real earth
 * WebAutoConfiguration.class
 * @Date 2019/05/23 上午11:24
 */
@EnableApolloConfig
@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = "com.mohism.pudding.gateway.modular.consumer")
public class PuddingGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PuddingGatewayApplication.class, args);
    }

}
