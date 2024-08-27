package com.example.storyrealm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private StoryAdapter storyAdapter;
    private List<Story> storyList;
    private EditText searchEditText;
    private ImageButton searchButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();
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

        db.collection("stories")
                .whereEqualTo("title", query)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Story> searchedStories = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Story story = document.toObject(Story.class);
                        searchedStories.add(story);
                    }
                    updateStoryList(searchedStories);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to fetch stories", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchUpdatedStories() {
        db.collection("stories")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Story> updatedStories = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Story story = document.toObject(Story.class);
                        updatedStories.add(story);
                        Log.d("HomeFragment", "Fetched story: " + story.getTitle());
                    }
                    updateStoryList(updatedStories);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to fetch updated stories", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateStoryList(List<Story> newStories) {
        storyList.clear();
        storyList.addAll(newStories);
        storyAdapter.notifyDataSetChanged();
    }
}
