package com.example.storyrealm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.storyrealm.R;
import com.example.storyrealm.adapters.DraftAdapter;
import com.example.storyrealm.models.Chapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class DraftManagementActivity extends AppCompatActivity {

    private ListView listViewDrafts;
    private Button btnAddChapter;
    private ArrayList<Chapter> draftList; // Use Chapter class
    private DraftAdapter draftAdapter; // Ensure DraftAdapter is designed to handle Chapter objects

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_management);

        listViewDrafts = findViewById(R.id.list_view_drafts);
        btnAddChapter = findViewById(R.id.btn_add_chapter);

        draftList = new ArrayList<>();
        draftAdapter = new DraftAdapter(this, draftList);
        listViewDrafts.setAdapter(draftAdapter);

        loadDrafts();

        btnAddChapter.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChapterCreationActivity.class);
            intent.putExtra("story_id", getIntent().getStringExtra("story_id")); // Pass story ID
            startActivity(intent);
        });

        listViewDrafts.setOnItemClickListener((parent, view, position, id) -> {
            Chapter chapter = draftList.get(position);
            // Handle click to edit/delete chapter (implement as needed)
            Toast.makeText(this, "Edit or delete " + chapter.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loadDrafts() {
        String storyId = getIntent().getStringExtra("story_id");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("stories").child(storyId).child("chapters").orderByChild("status").equalTo("draft")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        draftList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Chapter chapter = snapshot.getValue(Chapter.class);
                            draftList.add(chapter);
                        }
                        draftAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(DraftManagementActivity.this, "Failed to load drafts", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

