package com.fada.tweetfetcher.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "tweet")
public class Tweet {

    @Id
    private Long id;
    private String content;
    private String createdAt;
    private String type;

    public Tweet() {}

    public Tweet(Long id, String content, String createdAt, String type) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Id: " + id + "; Content: " + content + "; Created At: " + createdAt + "; Type: " + type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tweet tweet = (Tweet) o;
        return Objects.equals(id, tweet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
