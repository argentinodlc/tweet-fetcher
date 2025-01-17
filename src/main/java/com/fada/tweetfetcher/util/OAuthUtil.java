package com.fada.tweetfetcher.util;

import java.util.Base64;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class OAuthUtil {

    public static String getBearerToken(String consumerKey, String consumerSecret) throws Exception {
        String credentials = consumerKey + ":" + consumerSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedCredentials);
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        HttpEntity<String> entity = new HttpEntity<>("grant_type=client_credentials", headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.twitter.com/oauth2/token",
                HttpMethod.POST,
                entity,
                String.class
        );

        // Extract the access token from the response
        Map<String, Object> responseData = new ObjectMapper().readValue(response.getBody(), Map.class);
        return (String) responseData.get("access_token");
    }
}