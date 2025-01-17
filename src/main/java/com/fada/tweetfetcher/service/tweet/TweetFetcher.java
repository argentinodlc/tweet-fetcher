package com.fada.tweetfetcher.service.tweet;

import com.fada.tweetfetcher.client.TwitterApiClient;
import com.fada.tweetfetcher.model.Tweet;
import com.fada.tweetfetcher.util.EncodeUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TweetFetcher {

    private static final Logger logger = LoggerFactory.getLogger(TweetFetcher.class);

    private final TwitterApiClient twitterApiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${twitter.user.id}")
    private String USER_ID;

    public TweetFetcher(TwitterApiClient twitterApiClient) {
        this.twitterApiClient = twitterApiClient;
    }

    public List<Tweet> fetchTweets() throws IOException {
        List<Tweet> tweets = new ArrayList<>();
        String startTime = java.time.ZonedDateTime.now(java.time.ZoneOffset.UTC).toLocalDate()
                .atStartOfDay(java.time.ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT).toString();

        String encodedUserId = EncodeUtil.encode(USER_ID);
        String encodedTweetFields = EncodeUtil.encode("referenced_tweets,created_at");
        String encodedStartTime = EncodeUtil.encode(startTime);

        Response response = twitterApiClient.getTweets(encodedUserId, 5, encodedTweetFields, encodedStartTime);

        if (response.status() == 429) {
            long resetTime = Long.parseLong(response.headers().get("x-rate-limit-reset").iterator().next());
            long waitTime = (resetTime * 1000) - System.currentTimeMillis();
            logger.warn("Rate limit exceeded, wait time: {}s", waitTime / 1000);
        } else if (response.status() == 200) {
            Map<String, Object> responseData = objectMapper.readValue(response.body().asInputStream(), Map.class);
            List<Map<String, Object>> tweetData = (List<Map<String, Object>>) responseData.get("data");
            for (Map<String, Object> tweet : tweetData) {
                Tweet newTweet = new Tweet();
                newTweet.setId(Long.parseLong((String) tweet.get("id")));
                newTweet.setContent((String) tweet.get("text"));
                newTweet.setCreatedAt((String) tweet.get("created_at"));
                List<Map<String, Object>> referencedTweets = (List<Map<String, Object>>) tweet.get("referenced_tweets");
                if (referencedTweets != null && !referencedTweets.isEmpty()) {
                    newTweet.setType((String) referencedTweets.get(0).get("type"));
                } else {
                    newTweet.setType("tweet");
                }
                tweets.add(newTweet);
            }
        } else {
            logger.warn("Failed to get tweets, response code: {}", response.status());
        }
        return tweets;
    }
}