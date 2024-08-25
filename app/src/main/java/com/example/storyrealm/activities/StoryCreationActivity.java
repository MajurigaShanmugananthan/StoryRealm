package com.example.storyrealm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.storyrealm.R;
import com.example.storyrealm.models.Story;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class StoryCreationActivity extends AppCompatActivity {

    private EditText edtStoryTitle, edtStoryDescription;
    private Button btnNext;
    private DatabaseReference databaseStories;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_creation);

        edtStoryTitle = findViewById(R.id.edt_story_title);
        edtStoryDescription = findViewById(R.id.edt_story_description);
        btnNext = findViewById(R.id.btn_next);

        databaseStories = FirebaseDatabase.getInstance().getReference("stories");

        btnNext.setOnClickListener(v -> {
            String title = edtStoryTitle.getText().toString();
            String description = edtStoryDescription.getText().toString();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || description.length() > 1000) {
                Toast.makeText(this, "Please enter a valid title and description (max 1000 characters)", Toast.LENGTH_SHORT).show();
            } else {
                // Generate a unique ID for the story
                String storyId = UUID.randomUUID().toString();
                // Create a new Story object
                Story story = new Story(storyId, title, description, "draft");

                // Save the story to Firebase
                databaseStories.child(storyId).setValue(story).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(this, ChapterCreationActivity.class);
                        intent.putExtra("story_id", storyId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Failed to save story", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}


