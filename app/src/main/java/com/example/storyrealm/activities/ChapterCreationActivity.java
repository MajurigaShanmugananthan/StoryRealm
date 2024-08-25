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
import com.example.storyrealm.models.Chapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class ChapterCreationActivity extends AppCompatActivity {

    private EditText edtChapterTitle, edtChapterContent;
    private Button btnSave, btnPublish;
    private String storyId; // Assume storyId is passed from StoryCreationActivity

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_creation);

        edtChapterTitle = findViewById(R.id.edt_chapter_title);
        edtChapterContent = findViewById(R.id.edt_chapter_content);
        btnSave = findViewById(R.id.btn_save);
        btnPublish = findViewById(R.id.btn_publish);

        storyId = getIntent().getStringExtra("story_id");

        btnSave.setOnClickListener(v -> saveChapter(false));
        btnPublish.setOnClickListener(v -> saveChapter(true));
    }

// Inside ChapterCreationActivity.java

    private void saveChapter(boolean publish) {
        String title = edtChapterTitle.getText().toString();
        String content = edtChapterContent.getText().toString();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Please enter both title and content", Toast.LENGTH_SHORT).show();
        } else if (content.length() > 1000) { // Check if content exceeds 1000 words
            Toast.makeText(this, "Chapter content cannot exceed 1000 words", Toast.LENGTH_SHORT).show();
        } else {
            // Save the chapter to the database
            saveChapterToDatabase(title, content, publish ? "published" : "draft");

            // Navigate to the draft management screen
            Intent intent = new Intent(this, DraftManagementActivity.class);
            startActivity(intent);
        }
    }

    private void saveChapterToDatabase(String title, String content, String status) {
        // Assuming you have a method to get the Firebase database reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("chapters");

        String chapterId = databaseReference.push().getKey(); // Generate a unique ID
        Chapter chapter = new Chapter(title, content, status);
        databaseReference.child(chapterId).setValue(chapter).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ChapterCreationActivity.this, "Chapter saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChapterCreationActivity.this, "Failed to save chapter", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

