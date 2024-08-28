package com.example.storyrealm.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storyrealm.R;
import com.example.storyrealm.activities.StoryDetailActivity3;
import com.example.storyrealm.adapters.NotificationAdapter;
import com.example.storyrealm.models.Story;
import com.example.storyrealm.models.Subscription;
import com.example.storyrealm.utils.NotificationUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private RecyclerView notificationRecyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Story> notifications;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        notificationRecyclerView = view.findViewById(R.id.notification_recycler_view);
        notifications = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(notifications, story -> {
            Intent intent = new Intent(getActivity(), StoryDetailActivity3.class);
            intent.putExtra("story_id", story.getId());
            startActivity(intent);
        });

        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationRecyclerView.setAdapter(notificationAdapter);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadSubscriptions(userId);

        return view;
    }

    private void loadSubscriptions(String userId) {
        DatabaseReference subscriptionRef = FirebaseDatabase.getInstance().getReference("subscriptions");
        subscriptionRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> subscribedAuthorIds = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Subscription subscription = snapshot.getValue(Subscription.class);
                    subscribedAuthorIds.add(subscription.getAuthorId());
                }
                loadNotifications(subscribedAuthorIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load subscriptions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNotifications(List<String> subscribedAuthorIds) {
        DatabaseReference storiesRef = FirebaseDatabase.getInstance().getReference("stories");

        for (String authorId : subscribedAuthorIds) {
            storiesRef.orderByChild("authorId").equalTo(authorId).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                    Story story = dataSnapshot.getValue(Story.class);
                    if (story != null && !notifications.contains(story)) {
                        notifications.add(story);
                        notificationAdapter.notifyDataSetChanged();

                        // Send a local notification
                        new Thread(() -> NotificationUtils.sendNotification(getContext(), "New Story", "A new story titled '" + story.getTitle() + "' has been published.")).start();
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                    // Handle updates if necessary
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    // Handle removals if necessary
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                    // Handle moves if necessary
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Failed to load notifications", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
