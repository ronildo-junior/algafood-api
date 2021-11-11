package com.ronijr.algafoodapi.config.web;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Hidden
@RestController
public class HostCheckerController {
    @GetMapping("/host-check")
    public String hostCheck() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress() +
                " - " +
                InetAddress.getLocalHost().getHostName();
    }
}