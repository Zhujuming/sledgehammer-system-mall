package com.sledgehammer.order.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单控制器类
 * 用于处理与订单相关的请求和业务逻辑
 */
@RefreshScope
@RestController
@RequestMapping("/order")
public class OrderController {

    @Value("${config.info}")
   private String configInfo;

    @GetMapping("/test")
    public String test() {
        return "this is order service";
    }

    @GetMapping("/test/getConfigInfo")
    public String getConfigInfo() {
        return configInfo;
    }
}
