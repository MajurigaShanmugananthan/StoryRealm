package com.example.storyrealm.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.storyrealm.R;
import com.example.storyrealm.models.Story;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StoryDetailActivity3 extends AppCompatActivity {

    private TextView storyTitle;
    private TextView storyContent;
    private Button unsubscribeButton;
    private String authorId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail3);

        storyTitle = findViewById(R.id.story_title);
        storyContent = findViewById(R.id.story_content);
        unsubscribeButton = findViewById(R.id.unsubscribe_button);

        // Retrieve the story object passed via intent
        Story story = getIntent().getParcelableExtra("story");
        if (story != null) {
            // Populate the UI with story details
            storyTitle.setText(story.getTitle());
            storyContent.setText(story.getContent());
            authorId = story.getAuthorId();  // Store the author's ID

            unsubscribeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unsubscribeFromAuthor(authorId);
                }
            });
        } else {
            Toast.makeText(this, "No story data available", Toast.LENGTH_SHORT).show();
        }
    }

    private void unsubscribeFromAuthor(String authorId) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference subscriptionRef = FirebaseDatabase.getInstance().getReference("subscriptions");

        // Remove the specific author subscription
        subscriptionRef.child(userId).orderByChild("authorId").equalTo(authorId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    snapshot.getRef().removeValue().addOnCompleteListener(removeTask -> {
                        if (removeTask.isSuccessful()) {
                            Toast.makeText(StoryDetailActivity3.this, "Unsubscribed from author", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity and return to the previous screen
                        } else {
                            Toast.makeText(StoryDetailActivity3.this, "Failed to unsubscribe", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(StoryDetailActivity3.this, "Failed to find subscription", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
