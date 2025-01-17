package com.fada.tweetfetcher.service.tweet;

import com.fada.tweetfetcher.model.Tweet;
import com.fada.tweetfetcher.repository.TweetRepository;
import com.fada.tweetfetcher.service.tweet.kafka.TweetSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TweetService {

    private static final Logger logger = LoggerFactory.getLogger(TweetService.class);

    private final TweetFetcher tweetFetcher;
    private final TweetRepository tweetRepository;
    private final TweetSender tweetSender;

    public TweetService(TweetFetcher tweetFetcher, TweetRepository tweetRepository, TweetSender tweetSender) {
        this.tweetFetcher = tweetFetcher;
        this.tweetRepository = tweetRepository;
        this.tweetSender = tweetSender;
    }

    @Scheduled(fixedRate = 60000)
    public void processTweets() throws IOException {
        List<Tweet> tweets = tweetFetcher.fetchTweets();
        for (Tweet tweet : tweets) {
            if (!tweetRepository.existsById(tweet.getId())) {
                tweetRepository.save(tweet);
                tweetSender.sendTweet(tweet);
                logger.info("New tweet sent to Kafka: {}", tweet);
            } else {
                logger.info("Duplicate tweet ignored: {}", tweet);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void clearCache() {
        tweetRepository.deleteAll();
        logger.info("Tweet cache cleared at midnight");
    }
}