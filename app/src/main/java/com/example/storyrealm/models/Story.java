package com.example.storyrealm.models;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Story {
    private String id;
    private String authorId;
    private String title;
    private String content;
    private long timestamp;

    // Default constructor required for calls to DataSnapshot.getValue(Story.class)
    public Story() {
    }

    public Story(String id, String title, String content, String authorId, long timestamp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.timestamp = timestamp;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Exclude
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("authorId", authorId);
        result.put("title", title);
        result.put("content", content);
        result.put("timestamp", timestamp);
        return result;
    }
}
