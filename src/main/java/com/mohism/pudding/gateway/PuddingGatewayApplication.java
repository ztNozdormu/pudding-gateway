package com.mohism.pudding.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关服务
 *
 * @author fengshuonan
 * ,WebAutoConfiguration.class
 * @Date 2017/11/10 上午11:24
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@EnableFeignClients(basePackages = "cn.stylefeng.roses.gateway.modular.consumer")
@EnableDiscoveryClient
public class PuddingGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PuddingGatewayApplication.class, args);
    }

}
