package com.example.storyrealm.services;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationService {

    private DatabaseReference notificationRef;

    public NotificationService() {
        notificationRef = FirebaseDatabase.getInstance().getReference("notifications");
    }

    public void subscribeToCategory(String categoryId, ValueEventListener listener) {
        notificationRef.child(categoryId).addValueEventListener(listener);
    }

    public void sendNotification(String categoryId, String message) {
        String notificationId = notificationRef.child(categoryId).push().getKey();
        if (notificationId != null) {
            notificationRef.child(categoryId).child(notificationId).setValue(message);
        }
    }
}
