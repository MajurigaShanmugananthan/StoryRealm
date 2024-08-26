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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText edtSearch;
    private Button btnSearch;
    private RecyclerView recyclerView;
    private StoryAdapter storyAdapter;
    private List<Story> storyList;
    private DatabaseReference storyRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        edtSearch = view.findViewById(R.id.search_field);
        btnSearch = view.findViewById(R.id.search_button);
        recyclerView = view.findViewById(R.id.recycler_view);

        storyList = new ArrayList<>();
        storyAdapter = new StoryAdapter(storyList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(storyAdapter);

        storyRef = FirebaseUtils.getStoryReference();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchStories();
            }
        });

        return view;
    }

    private void searchStories() {
        String searchText = edtSearch.getText().toString().trim();

        if (TextUtils.isEmpty(searchText)) {
            Toast.makeText(getContext(), "Please enter a search term", Toast.LENGTH_SHORT).show();
            return;
        }

        storyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                storyList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Story story = snapshot.getValue(Story.class);
                    if (story != null) {
                        String title = story.getTitle();
                        if (title != null && title.toLowerCase().contains(searchText.toLowerCase())) {
                            storyList.add(story);
                        }
                    }
                }
                storyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error loading stories", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
