package com.sledgehammer.order.controller;

import com.sledgehammer.module.entity.OrderDO;
import com.sledgehammer.order.feignClient.PayServiceFeignClient;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    @Resource
    private PayServiceFeignClient payServiceFeignClient;

    @GetMapping("/test")
    public String test() {
        return "this is order service";
    }

    @GetMapping("/test/getConfigInfo")
    public String getConfigInfo() {
        return configInfo;
    }

    @PostMapping("/addOrder")
    public String addOrder() {
        OrderDO order = new OrderDO();
        order.setId(123L);
        order.setProductName("桌面小风扇");
        String addPayType = payServiceFeignClient.addPayType(order);
        return addPayType + "add order success";
    }
}
