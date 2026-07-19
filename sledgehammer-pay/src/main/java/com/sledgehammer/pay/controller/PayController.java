package com.sledgehammer.pay.controller;

import com.sledgehammer.module.entity.OrderDO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class PayController {

    @RequestMapping("/test")
    public String test() {
        return "this is pay-service";
    }
    @RequestMapping("/addPayType")
    public String addPayType(@RequestBody OrderDO orderDO) {
        return "add payType success !!! 商品名称：" + orderDO.getProductName();
    }
}
