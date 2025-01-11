package com.payment.connector;

import com.payment.model.user.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-connector", url = "${connector.auth.url}")
public interface AuthConnector {
    @GetMapping(path = "/getCurrentUser")
    User getCurrentUser(@RequestHeader(name = "Authorization") String token);
}
