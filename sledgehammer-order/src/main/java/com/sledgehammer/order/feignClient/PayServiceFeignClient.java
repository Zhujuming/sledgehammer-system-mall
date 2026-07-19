package com.sledgehammer.order.feignClient;

import com.sledgehammer.module.entity.OrderDO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sledgehammer-pay-service")
public interface PayServiceFeignClient {
    @PostMapping(value = "/pay/addPayType")
    public String addPayType(@RequestBody OrderDO orderDO);
}
