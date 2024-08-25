package com.example.storyrealm.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText searchField;
    private Button searchButton;
    private RecyclerView recyclerView;
    private StoryAdapter adapter;
    private List<Story> stories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchField = view.findViewById(R.id.search_field);
        searchButton = view.findViewById(R.id.search_button);
        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        stories = new ArrayList<>();
        adapter = new StoryAdapter(stories);
        recyclerView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> searchStories());

        return view;
    }

    private void searchStories() {
        String query = searchField.getText().toString();
        if (TextUtils.isEmpty(query)) {
            Toast.makeText(getContext(), "Please enter a story name", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUtils.getDatabase().getReference("stories")
                .orderByChild("title")
                .startAt(query)
                .endAt(query + "\uf8ff")
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
                    } else {
                        Toast.makeText(getContext(), "Search failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
