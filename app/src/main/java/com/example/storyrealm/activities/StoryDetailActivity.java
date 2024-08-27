package com.example.storyrealm.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.storyrealm.R;
import com.example.storyrealm.models.Story;
import com.example.storyrealm.repositories.StoryRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StoryDetailActivity extends AppCompatActivity {

    private TextView storyTitleTextView;
    private TextView storyContentTextView;
    private Button deleteButton;
    private StoryRepository storyRepository;
    private String storyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        storyTitleTextView = findViewById(R.id.story_title);
        storyContentTextView = findViewById(R.id.story_content);
        deleteButton = findViewById(R.id.delete_button);

        storyRepository = new StoryRepository();

        storyId = getIntent().getStringExtra("story_id");

        if (storyId != null) {
            fetchStoryDetails(storyId);
        }

        deleteButton.setOnClickListener(v -> {
            if (storyId != null) {
                storyRepository.deleteStory(storyId);
                Toast.makeText(this, "Story deleted", Toast.LENGTH_SHORT).show();
                finish(); // Return to the previous activity
            }
        });
    }

    private void fetchStoryDetails(String storyId) {
        DatabaseReference storyRef = FirebaseDatabase.getInstance().getReference("stories").child(storyId);
        storyRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Story story = task.getResult().getValue(Story.class);
                if (story != null) {
                    storyTitleTextView.setText(story.getTitle());
                    storyContentTextView.setText(story.getContent());
                }
            } else {
                Toast.makeText(this, "Failed to load story details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteStory(String storyId) {
        DatabaseReference storyRef = FirebaseDatabase.getInstance().getReference("stories").child(storyId);
        storyRef.removeValue().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(this, "Failed to delete story", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
