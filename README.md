# Tweet Fetcher Microservice

## Prerequisites

- Java 17
- Kafka

## Configuration
Create a [`.env`](.env ) file in the root directory with the following content:
    
```env
DB_USERNAME=<h2 username>
DB_PASSWORD=<h2 password>
TWITTER_USER_ID=<twitter user ID for the user you want to fetch tweets from>
TWITTER_CONSUMER_KEY=<twitter API consumer key>
TWITTER_CONSUMER_SECRET=<twitter API consumer secret>
TWITTER_TOKEN_KEY=<twitter API token key>
TWITTER_TOKEN_SECRET=<twitter API token secret>
```

> Alternatively, you can set these environment variables directly in your shell or deployment environment.

## Usage

Start the application:
    
```shell
./mvnw spring-boot:run
```
The application will fetch tweets at a fixed rate of 1 minute from the specified user on Twitter and store them in the H2 database. 
It will also send the non-stored tweets to the Kafka topic tweet-fetcher-tweets.
When the application sends new tweets to the Kafka topic, the messages will have the following structure:

```json
{
  "id": 1,
  "text": "tweet text",
  "createdAt": "2025-01-01T00:00:00Z",
  "type": "tweet"
}
```

The database is cleared at midnight every day.

