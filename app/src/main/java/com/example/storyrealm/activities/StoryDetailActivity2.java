package com.example.storyrealm.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.storyrealm.R;
import com.example.storyrealm.models.Story;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class StoryDetailActivity2 extends AppCompatActivity {

    private static final String TAG = "StoryDetailActivity2";
    private TextView storyTitleTextView;
    private TextView storyContentTextView;
    private FirebaseDatabase database;
    private DatabaseReference storyRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail2);

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance();
        storyRef = database.getReference("stories");

        // Find the TextViews in the layout
        storyTitleTextView = findViewById(R.id.story_title);
        storyContentTextView = findViewById(R.id.story_content);

        // Retrieve story ID from Intent
        String storyId = getIntent().getStringExtra("story_id");

        // Load story details
        if (storyId != null) {
            loadStoryDetails(storyId);
        } else {
            storyTitleTextView.setText("Story ID not found");
            storyContentTextView.setText("");
        }
    }

    private void loadStoryDetails(String storyId) {
        storyRef.child(storyId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Story story = dataSnapshot.getValue(Story.class);
                    if (story != null) {
                        storyTitleTextView.setText(story.getTitle());
                        storyContentTextView.setText(story.getContent());
                    } else {
                        storyTitleTextView.setText("Story not found");
                        storyContentTextView.setText("");
                    }
                } else {
                    storyTitleTextView.setText("Story not found");
                    storyContentTextView.setText("");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Failed to load story details", databaseError.toException());
                storyTitleTextView.setText("Error loading story");
                storyContentTextView.setText("");
            }
        });
    }
}
