package com.example.storyrealm.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.storyrealm.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditChapterActivity extends AppCompatActivity {

    private EditText edtChapterTitle, edtChapterContent;
    private Button btnSave, btnDelete;
    private DatabaseReference databaseChapters;
    private String chapterId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_chapter);

        edtChapterTitle = findViewById(R.id.edt_chapter_title);
        edtChapterContent = findViewById(R.id.edt_chapter_content);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);

        databaseChapters = FirebaseDatabase.getInstance().getReference("chapters");
        chapterId = getIntent().getStringExtra("chapter_id");

        // Load chapter details (placeholder)
        loadChapterDetails();

        btnSave.setOnClickListener(v -> saveChapter());
        btnDelete.setOnClickListener(v -> deleteChapter());
    }

    private void loadChapterDetails() {
        // Load chapter details from database and populate fields (placeholder)
    }

    private void saveChapter() {
        String title = edtChapterTitle.getText().toString();
        String content = edtChapterContent.getText().toString();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content) || content.length() > 1000) {
            Toast.makeText(this, "Please enter valid title and content (max 1000 characters)", Toast.LENGTH_SHORT).show();
        } else {
            databaseChapters.child(chapterId).child("title").setValue(title);
            databaseChapters.child(chapterId).child("content").setValue(content);
            Toast.makeText(this, "Chapter Updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteChapter() {
        databaseChapters.child(chapterId).removeValue();
        Toast.makeText(this, "Chapter Deleted", Toast.LENGTH_SHORT).show();
        finish();
    }
}
