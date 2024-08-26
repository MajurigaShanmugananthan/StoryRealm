package com.example.storyrealm.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.storyrealm.R;
import com.example.storyrealm.models.Story;
import com.example.storyrealm.utils.FirebaseUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WriteFragment extends Fragment {

    private EditText edtStoryTitle;
    private EditText edtStoryContent;
    private Button btnSave;

    private DatabaseReference storyRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write, container, false);

        edtStoryTitle = view.findViewById(R.id.edt_story_title);
        edtStoryContent = view.findViewById(R.id.edt_story_content);
        btnSave = view.findViewById(R.id.btn_save);

        storyRef = FirebaseUtils.getStoryReference();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStory();
            }
        });

        return view;
    }

    private void saveStory() {
        String title = edtStoryTitle.getText().toString().trim();
        String content = edtStoryContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(getContext(), "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.length() > 1000) {
            Toast.makeText(getContext(), "Story content cannot exceed 1000 words", Toast.LENGTH_SHORT).show();
            return;
        }

        String storyId = UUID.randomUUID().toString();
        String authorId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        long timestamp = System.currentTimeMillis();

        Story story = new Story(storyId, title, content, authorId, timestamp);

        Map<String, Object> storyValues = story.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/stories/" + storyId, storyValues);

        storyRef.updateChildren(childUpdates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Story saved successfully", Toast.LENGTH_SHORT).show();
                    edtStoryTitle.setText("");
                    edtStoryContent.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to save story", Toast.LENGTH_SHORT).show();
                });
    }
}
