package com.example.storyrealm.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.storyrealm.R;
import com.example.storyrealm.models.Story;
import com.example.storyrealm.models.Subscription;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StoryDetailActivity2 extends AppCompatActivity {

    private static final String TAG = "StoryDetailActivity2";
    private TextView storyTitleTextView;
    private TextView storyContentTextView;
    private Button subscribeButton;
    private FirebaseDatabase database;
    private DatabaseReference storyRef;
    private DatabaseReference subscriptionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail2);

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance();
        storyRef = database.getReference("stories");
        subscriptionRef = database.getReference("subscriptions");

        // Find the views in the layout
        storyTitleTextView = findViewById(R.id.story_title);
        storyContentTextView = findViewById(R.id.story_content);
        subscribeButton = findViewById(R.id.subscribe_button);

        // Retrieve story ID from Intent
        String storyId = getIntent().getStringExtra("story_id");

        // Load story details
        if (storyId != null) {
            loadStoryDetails(storyId);
        } else {
            storyTitleTextView.setText("Story ID not found");
            storyContentTextView.setText("");
        }

        // Set up subscribe button
        subscribeButton.setOnClickListener(v -> subscribeToAuthor());
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
                        subscribeButton.setTag(story.getAuthorId()); // Store author ID in the button tag
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

    private void subscribeToAuthor() {
        String authorId = (String) subscribeButton.getTag();
        if (authorId != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Subscription subscription = new Subscription(authorId, userId);

            subscriptionRef.child(userId).child(authorId).setValue(subscription)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Subscribed to author!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Subscription failed.", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Author ID not found", Toast.LENGTH_SHORT).show();
        }
    }
}
