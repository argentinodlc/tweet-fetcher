package com.fada.tweetfetcher.config;

import com.fada.tweetfetcher.util.OAuthUtil;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class TwitterApiClientConfig {

    @Value("${twitter.consumer.key}")
    private String consumerKey;

    @Value("${twitter.consumer.secret}")
    private String consumerSecret;

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return requestTemplate -> {
            try {
                String bearerToken = OAuthUtil.getBearerToken(consumerKey, consumerSecret);
                requestTemplate.header("Authorization", "Bearer " + bearerToken);
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate Bearer token", e);
            }
        };
    }
}