package com.carrental.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "payment-service",
        url = "${payment.service.url}"
)
public interface PaymentClient {

    @PostMapping("/api/payments/wallet/create")
    void createWallet(
            @RequestParam("ownerEmail") String ownerEmail,
            @RequestParam("ownerId") Long ownerId
    );
}
