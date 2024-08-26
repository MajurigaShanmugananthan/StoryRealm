package com.example.storyrealm.repositories;

import com.example.storyrealm.models.Story;
import com.example.storyrealm.utils.FirebaseUtils;
import com.google.firebase.database.DatabaseReference;

public class StoryRepository {

    private DatabaseReference storyRef;

    public StoryRepository() {
        storyRef = FirebaseUtils.getStoryReference();
    }

    // Save a new story or update an existing story
    public void saveStory(Story story) {
        String storyId = story.getId();
        if (storyId != null) {
            // Save or update the story in Firebase
            storyRef.child(storyId).setValue(story)
                    .addOnSuccessListener(aVoid -> {
                        // Handle success
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                    });
        }
    }

    // Update an existing story
    public void updateStory(String storyId, Story updatedStory) {
        if (storyId != null) {
            // Update the story in Firebase
            storyRef.child(storyId).updateChildren(updatedStory.toMap())
                    .addOnSuccessListener(aVoid -> {
                        // Handle success
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                    });
        }
    }

    // Delete a story
    public void deleteStory(String storyId) {
        storyRef.child(storyId).removeValue();
    }

    // Get a reference to all stories
    public DatabaseReference getAllStories() {
        return storyRef;
    }
}
