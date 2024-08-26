package com.example.storyrealm.models;

import java.util.HashMap;
import java.util.Map;

public class Story {

    private String id;
    private String authorId;
    private String content;
    private String title;
    private long timestamp;

    // Default constructor required for Firebase
    public Story() {
    }

    public Story(String id, String authorId, String content, String title, long timestamp) {
        this.id = id;
        this.authorId = authorId;
        this.content = content;
        this.title = title;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Convert Story to a Map
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("authorId", authorId);
        result.put("content", content);
        result.put("title", title);
        result.put("timestamp", timestamp);
        return result;
    }
}
