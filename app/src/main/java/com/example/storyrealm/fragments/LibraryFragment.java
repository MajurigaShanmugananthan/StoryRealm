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
import com.example.storyrealm.utils.FirebaseUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {

    private RecyclerView recyclerView;
    private StoryAdapter adapter;
    private List<Story> stories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        stories = new ArrayList<>();
        adapter = new StoryAdapter(stories);
        recyclerView.setAdapter(adapter);

        loadUserStories();

        return view;
    }

    private void loadUserStories() {
        String userId = FirebaseUtils.getAuth().getCurrentUser().getUid();
        FirebaseUtils.getDatabase().getReference("stories")
                .orderByChild("authorId")
                .equalTo(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        stories.clear();
                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
                            Story story = snapshot.getValue(Story.class);
                            if (story != null) {
                                stories.add(story);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
