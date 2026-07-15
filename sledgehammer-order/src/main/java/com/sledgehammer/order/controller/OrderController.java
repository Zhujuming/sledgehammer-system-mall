package com.sledgehammer.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单控制器类
 * 用于处理与订单相关的请求和业务逻辑
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @GetMapping("/test")
    public String test() {
        return "this is order service";
    }
}
