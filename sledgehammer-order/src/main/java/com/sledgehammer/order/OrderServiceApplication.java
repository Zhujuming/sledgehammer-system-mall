package com.sledgehammer.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Spring Boot应用程序的主入口类
 * 使用@SpringBootApplication注解标记这是一个Spring Boot应用
 */
@RefreshScope  // 使配置文件热加载（Nacos动态刷新）
@SpringBootApplication
public class OrderServiceApplication {

    /**
     * 程序的主方法，应用程序的执行起点
     * @param args 命令行参数
     */
	public static void main(String[] args) {
        // 使用SpringApplication.run()方法启动Spring Boot应用程序
		SpringApplication.run(OrderServiceApplication.class, args);
	}

}
