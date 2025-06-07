package com.gateway.controller;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SwaggerController {

    private final DiscoveryClient discoveryClient;

    public SwaggerController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/v3/api-docs")
    public Map<String, String> apiDocs() {
        return discoveryClient.getServices().stream()
                .filter(service -> service.contains("service"))
                .collect(Collectors.toMap(
                        service -> service.replace("-", " ").toUpperCase(),
                        service -> "/" + service + "/v3/api-docs"
                ));
    }

}
