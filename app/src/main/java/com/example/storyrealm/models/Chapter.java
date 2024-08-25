package com.example.storyrealm.models;

public class Chapter {
    private String title;
    private String content;
    private String status; // e.g., "draft" or "published"

    // Default constructor required for Firebase
    public Chapter() {
    }

    // Constructor with parameters
    public Chapter(String title, String content, String status) {
        this.title = title;
        this.content = content;
        this.status = status;
    }

    // Getters and setters
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
