package com.fada.tweetfetcher.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.fada.tweetfetcher.client")
public class FeignConfig {
}

