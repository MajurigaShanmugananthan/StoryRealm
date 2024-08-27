package com.example.storyrealm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storyrealm.R;
import com.example.storyrealm.activities.StoryDetailActivity2;
import com.example.storyrealm.adapters.StoryAdapter;
import com.example.storyrealm.models.Story;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference storiesRef;
    private RecyclerView recyclerView;
    private StoryAdapter storyAdapter;
    private List<Story> storyList;
    private EditText searchEditText;
    private ImageButton searchButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        database = FirebaseDatabase.getInstance();
        storiesRef = database.getReference("stories");

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchEditText = view.findViewById(R.id.search_edit_text);
        searchButton = view.findViewById(R.id.search_button);

        storyList = new ArrayList<>();
        storyAdapter = new StoryAdapter(storyList, story -> {
            Intent intent = new Intent(getContext(), StoryDetailActivity2.class);
            intent.putExtra("story_id", story.getId());
            startActivity(intent);
        });

        recyclerView.setAdapter(storyAdapter);

        searchButton.setOnClickListener(v -> searchStories());
        fetchUpdatedStories();

        return view;
    }

    private void searchStories() {
        String query = searchEditText.getText().toString().trim();
        if (TextUtils.isEmpty(query)) {
            Toast.makeText(getContext(), "Enter a story title to search", Toast.LENGTH_SHORT).show();
            return;
        }

        storiesRef.orderByChild("title").equalTo(query).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Story> searchedStories = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Story story = snapshot.getValue(Story.class);
                    if (story != null) {
                        searchedStories.add(story);
                    }
                }
                updateStoryList(searchedStories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                Toast.makeText(getContext(), "Failed to fetch stories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUpdatedStories() {
        storiesRef.orderByChild("timestamp").limitToLast(20).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Story> updatedStories = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Story story = snapshot.getValue(Story.class);
                    if (story != null) {
                        updatedStories.add(story);
                    }
                }
                updateStoryList(updatedStories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                Toast.makeText(getContext(), "Failed to fetch updated stories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStoryList(List<Story> newStories) {
        storyList.clear();
        storyList.addAll(newStories);
        storyAdapter.notifyDataSetChanged();
    }
}
