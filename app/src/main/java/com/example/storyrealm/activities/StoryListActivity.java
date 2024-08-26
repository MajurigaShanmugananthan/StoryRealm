package com.example.storyrealm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storyrealm.R;
import com.example.storyrealm.adapters.StoryAdapter;
import com.example.storyrealm.models.Story;
import com.example.storyrealm.repositories.StoryRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StoryListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StoryAdapter storyAdapter;
    private List<Story> storyList;
    private StoryRepository storyRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        storyList = new ArrayList<>();
        storyAdapter = new StoryAdapter(storyList);
        recyclerView.setAdapter(storyAdapter);

        storyRepository = new StoryRepository();

        storyRepository.getAllStories().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storyList.clear();
                for (DataSnapshot storySnapshot : dataSnapshot.getChildren()) {
                    Story story = storySnapshot.getValue(Story.class);
                    storyList.add(story);
                }
                storyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    public void onAddStoryClick(View view) {
        Intent intent = new Intent(this, StoryCreationActivity.class);
        startActivity(intent);
    }
}
