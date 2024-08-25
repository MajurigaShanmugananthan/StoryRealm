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
import com.example.storyrealm.adapters.NotificationAdapter;
import com.example.storyrealm.models.Notification;
import com.example.storyrealm.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notifications;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        notifications = new ArrayList<>();
        adapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(adapter);

        loadNotifications();

        return view;
    }

    private void loadNotifications() {
        String userId = FirebaseUtils.getAuth().getCurrentUser().getUid();
        FirebaseUtils.getDatabase().getReference("notifications")
                .child(userId)
                .orderByChild("timestamp")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notifications.clear();
                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
                            Notification notification = snapshot.getValue(Notification.class);
                            if (notification != null) {
                                notifications.add(notification);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
