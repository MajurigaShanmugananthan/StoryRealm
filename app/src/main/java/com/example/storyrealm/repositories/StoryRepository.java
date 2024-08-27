package com.example.storyrealm.repositories;

import com.example.storyrealm.models.Story;
import com.example.storyrealm.utils.FirebaseUtils;
import com.google.firebase.database.DatabaseReference;

public class StoryRepository {

    private final DatabaseReference storyRef;

    public StoryRepository() {
        storyRef = FirebaseUtils.getStoryReference();
    }

    public void saveStory(Story story) {
        String storyId = story.getId();
        if (storyId == null || storyId.isEmpty()) {
            storyId = storyRef.push().getKey(); // Generate a new unique ID
            story.setId(storyId);
        }
        storyRef.child(storyId).setValue(story)
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    public void updateStory(String storyId, Story updatedStory) {
        if (storyId != null) {
            storyRef.child(storyId).setValue(updatedStory)
                    .addOnSuccessListener(aVoid -> {
                        // Handle success
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                    });
        }
    }

    public void deleteStory(String storyId) {
        storyRef.child(storyId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    public DatabaseReference getAllStories() {
        return storyRef;
    }

    public DatabaseReference getStoryById(String storyId) {
        return storyRef.child(storyId);
    }
}
