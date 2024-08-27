package com.example.storyrealm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.storyrealm.R;
import com.example.storyrealm.fragments.WriteFragment;
import com.example.storyrealm.models.Story;
import com.example.storyrealm.repositories.StoryRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StoryCreationActivity extends AppCompatActivity {

    private EditText storyTitleEditText;
    private EditText storyContentEditText;
    private Button updateButton;
    private StoryRepository storyRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_creation);

        storyTitleEditText = findViewById(R.id.story_title_edit_text);
        storyContentEditText = findViewById(R.id.story_content_edit_text);
        updateButton = findViewById(R.id.update_button);

        storyRepository = new StoryRepository();

        updateButton.setOnClickListener(v -> {
            String title = storyTitleEditText.getText().toString().trim();
            String content = storyContentEditText.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (content.length() > 1000) {
                Toast.makeText(this, "Story content cannot exceed 1000 words", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference storyRef = FirebaseDatabase.getInstance().getReference("stories");

            String storyId = storyRef.push().getKey();
            Story newStory = new Story(storyId, title, content, userId, System.currentTimeMillis());

            if (storyId != null) {
                storyRef.child(storyId).setValue(newStory).addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Story added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add story", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
