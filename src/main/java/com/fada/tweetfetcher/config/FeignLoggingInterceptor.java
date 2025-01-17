package com.fada.tweetfetcher.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignLoggingInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(FeignLoggingInterceptor.class);

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                logger.debug("Request Method: {}", requestTemplate.method());
                logger.debug("Request URL: {}", requestTemplate.url());
                logger.debug("Request Headers: {}", requestTemplate.headers());
                if (requestTemplate.body() != null) {
                    logger.debug("Request Body: {}", new String(requestTemplate.body()));
                }
        }
        };
    }
}