package com.example.storyrealm.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.storyrealm.R;
import com.example.storyrealm.models.Story;
import com.example.storyrealm.repositories.StoryRepository;
import com.google.firebase.auth.FirebaseAuth;

public class StoryCreationActivity extends AppCompatActivity {

    private EditText edtStoryTitle, edtStoryContent;
    private Button btnSave;
    private StoryRepository storyRepository;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_creation);

        edtStoryTitle = findViewById(R.id.edt_story_title);
        edtStoryContent = findViewById(R.id.edt_story_content);
        btnSave = findViewById(R.id.btn_save);

        storyRepository = new StoryRepository();
        auth = FirebaseAuth.getInstance();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStory();
            }
        });
    }

    private void saveStory() {
        String title = edtStoryTitle.getText().toString().trim();
        String content = edtStoryContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.length() > 1000) {
            Toast.makeText(this, "Content exceeds the 1000-word limit", Toast.LENGTH_SHORT).show();
            return;
        }

        String authorId = auth.getCurrentUser().getUid();
        long timestamp = System.currentTimeMillis();

        Story story = new Story(null, title, content, authorId, timestamp);
        storyRepository.saveStory(story); // Use saveStory instead of addStory

        Toast.makeText(this, "Story saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
