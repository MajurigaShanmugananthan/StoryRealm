package com.example.storyrealm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.storyrealm.R;
import com.example.storyrealm.activities.StoryCreationActivity;

public class WriteFragment extends Fragment {

    private Button btnCreateStory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write, container, false);

        btnCreateStory = view.findViewById(R.id.btn_create_story);
        btnCreateStory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), StoryCreationActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
