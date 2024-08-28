package com.example.storyrealm.models;

public class Subscription {
    private String authorId;
    private String userId;

    public Subscription() {}

    public Subscription(String authorId, String userId) {
        this.authorId = authorId;
        this.userId = userId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
