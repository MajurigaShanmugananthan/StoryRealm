package com.example.storyrealm.services;

import com.google.firebase.analytics.FirebaseAnalytics;
import android.content.Context;
import android.os.Bundle;

public class AnalyticsService {
    private FirebaseAnalytics firebaseAnalytics;

    public AnalyticsService(Context context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public void logStoryReadEvent(String storyId, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, storyId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, title);
        firebaseAnalytics.logEvent("story_read", bundle);
    }

    public void logStoryCreatedEvent(String storyId, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, storyId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, title);
        firebaseAnalytics.logEvent("story_created", bundle);
    }

    public void logStoryViewEvent(String storyId, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, storyId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, title);
        firebaseAnalytics.logEvent("story_view", bundle);
    }

    public void logStoryLikeEvent(String storyId, String title) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, storyId);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, title);
        firebaseAnalytics.logEvent("story_like", bundle);
    }
}
