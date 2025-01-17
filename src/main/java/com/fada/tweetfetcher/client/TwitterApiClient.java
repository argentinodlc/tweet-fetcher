package com.fada.tweetfetcher.client;

import com.fada.tweetfetcher.config.TwitterApiClientConfig;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "twitterApiClient", url = "https://api.twitter.com", configuration = TwitterApiClientConfig.class)
public interface TwitterApiClient {

    @GetMapping("/2/users/{id}/tweets")
    Response getTweets(@PathVariable("id") String userId,
                       @RequestParam("max_results") int maxResults,
                       @RequestParam("tweet.fields") String tweetFields,
                       @RequestParam("start_time") String startTime
                       );

}