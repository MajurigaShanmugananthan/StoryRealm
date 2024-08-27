package com.example.storyrealm.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.storyrealm.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class StoryDetailActivity2 extends AppCompatActivity {

    private TextView storyTitleTextView;
    private TextView storyContentTextView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        // Initialize Firebase Fire store
        db = FirebaseFirestore.getInstance();

        // Find the TextViews in the layout
        storyTitleTextView = findViewById(R.id.story_title);
        storyContentTextView = findViewById(R.id.story_content);

        // Retrieve story ID from Intent
        String storyId = getIntent().getStringExtra("story_id");

        // Load story details
        if (storyId != null) {
            loadStoryDetails(storyId);
        }
    }

    private void loadStoryDetails(String storyId) {
        // Reference to the story document in Fire store
        db.collection("stories").document(storyId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Extract story data
                            String title = document.getString("title");
                            String content = document.getString("content");

                            // Update UI with story details
                            storyTitleTextView.setText(title);
                            storyContentTextView.setText(content);
                        } else {
                            // Handle the case where the document does not exist
                            storyTitleTextView.setText("Story not found");
                            storyContentTextView.setText("");
                        }
                    } else {
                        // Handle the error
                        storyTitleTextView.setText("Error loading story");
                        storyContentTextView.setText("");
                    }
                });
    }
}
