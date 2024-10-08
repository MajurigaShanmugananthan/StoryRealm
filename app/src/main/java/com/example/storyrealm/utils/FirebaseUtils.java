package com.example.storyrealm.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    public static FirebaseAuth getAuth() {
        return FirebaseAuth.getInstance();
    }

    public static FirebaseDatabase getDatabase() {
        return FirebaseDatabase.getInstance();
    }

    public static DatabaseReference getUsersReference() {
        return getDatabase().getReference("users");
    }

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference storyRef = database.getReference("stories");

    public static DatabaseReference getStoryReference() {
        return storyRef;
    }
}