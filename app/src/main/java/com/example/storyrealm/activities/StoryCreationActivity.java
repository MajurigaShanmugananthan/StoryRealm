package com.example.storyrealm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.storyrealm.R;
import com.example.storyrealm.models.Story;
import com.example.storyrealm.repositories.StoryRepository;
import com.example.storyrealm.services.AnalyticsService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StoryCreationActivity extends AppCompatActivity {

    private EditText storyTitleEditText;
    private EditText storyContentEditText;
    private Button updateButton;
    private StoryRepository storyRepository;
    private AnalyticsService analyticsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_creation);

        storyTitleEditText = findViewById(R.id.story_title_edit_text);
        storyContentEditText = findViewById(R.id.story_content_edit_text);
        updateButton = findViewById(R.id.update_button);

        storyRepository = new StoryRepository();
        analyticsService = new AnalyticsService(this);

        Intent intent = getIntent();
        String storyId = intent.getStringExtra("story_id");
        String storyTitle = intent.getStringExtra("story_title");
        String storyContent = intent.getStringExtra("story_content");

        if (storyTitle != null) {
            storyTitleEditText.setText(storyTitle);
        }

        if (storyContent != null) {
            storyContentEditText.setText(storyContent);
        }

        final String finalStoryId = storyId;  // Declare a final or effectively final variable

        updateButton.setOnClickListener(v -> {
            String title = storyTitleEditText.getText().toString().trim();
            String content = storyContentEditText.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            int wordCount = content.split("\\s+").length; // Split by whitespace and count words

            if (wordCount > 1000) {
                Toast.makeText(this, "Story content cannot exceed 1000 words", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference storyRef = FirebaseDatabase.getInstance().getReference("stories");

            String idToUse = finalStoryId != null ? finalStoryId : storyRef.push().getKey();  // Use final variable in lambda

            Story updatedStory = new Story(idToUse, title, content, userId, System.currentTimeMillis());

            if (idToUse != null) {
                storyRef.child(idToUse).setValue(updatedStory).addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Story updated successfully", Toast.LENGTH_SHORT).show();

                    // Log the story created event
                    analyticsService.logStoryCreatedEvent(idToUse, title);

                    // Set result and finish the activity
                    Intent resultIntent = new Intent();
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update story", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}