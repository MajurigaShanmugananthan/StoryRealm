package com.example.storyrealm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storyrealm.R;
import com.example.storyrealm.adapters.StoryAdapter;
import com.example.storyrealm.models.Story;
import com.google.firebase.auth.FirebaseAuth;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        storyList = new ArrayList<>();

        // Pass both the context and the story list to the StoryAdapter
        storyAdapter = new StoryAdapter(storyList);

        recyclerView.setAdapter(storyAdapter);

        fetchRecommendations();
        fetchTopStories();

        return view;
    }

    private void fetchRecommendations() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            List<String> preferredGenres = (List<String>) documentSnapshot.get("preferredGenres");

            if (preferredGenres != null) {
                db.collection("stories")
                        .whereArrayContainsAny("genre", preferredGenres)
                        .orderBy("likes", Query.Direction.DESCENDING)
                        .limit(10)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            List<Story> recommendedStories = new ArrayList<>();
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                Story story = document.toObject(Story.class);
                                recommendedStories.add(story);
                            }
                            updateStoryList(recommendedStories);
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure
                        });
            }
        }).addOnFailureListener(e -> {
            // Handle failure
        });
    }

    private void fetchTopStories() {
        db.collection("stories")
                .orderBy("likes", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Story> topStories = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Story story = document.toObject(Story.class);
                        topStories.add(story);
                    }
                    updateStoryList(topStories);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    private void updateStoryList(List<Story> newStories) {
        storyList.clear();
        storyList.addAll(newStories);
        storyAdapter.notifyDataSetChanged();
    }
}
