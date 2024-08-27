package com.example.storyrealm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storyrealm.R;
import com.example.storyrealm.activities.StoryCreationActivity;
import com.example.storyrealm.activities.StoryDetailActivity;
import com.example.storyrealm.adapters.StoryAdapter;
import com.example.storyrealm.models.Story;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WriteFragment extends Fragment {

    private RecyclerView storyRecyclerView;
    private Button addNewStoryButton;

    private DatabaseReference storiesRef;
    private StoryAdapter storyAdapter;
    private List<Story> userStories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write, container, false);

        storyRecyclerView = view.findViewById(R.id.recycler_view);
        addNewStoryButton = view.findViewById(R.id.add_story_button);

        userStories = new ArrayList<>();
        storyAdapter = new StoryAdapter(userStories, story -> {
            Intent intent = new Intent(getContext(), StoryDetailActivity.class);
            intent.putExtra("story_id", story.getId());
            startActivity(intent);
        });

        storyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        storyRecyclerView.setAdapter(storyAdapter);

        storiesRef = FirebaseDatabase.getInstance().getReference("stories");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadUserStories(userId);

        addNewStoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), StoryCreationActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadUserStories(String userId) {
        storiesRef.orderByChild("authorId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userStories.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Story story = snapshot.getValue(Story.class);
                    userStories.add(story);
                }
                storyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load stories", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
