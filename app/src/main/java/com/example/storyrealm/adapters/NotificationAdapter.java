package com.example.storyrealm.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storyrealm.R;
import com.example.storyrealm.activities.StoryDetailActivity3;
import com.example.storyrealm.models.Story;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Story> notifications;
    private OnItemClickListener onItemClickListener;

    public NotificationAdapter(List<Story> notifications, OnItemClickListener onItemClickListener) {
        this.notifications = notifications;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Story story = notifications.get(position);
        holder.storyTitle.setText(story.getTitle());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), StoryDetailActivity3.class);
            intent.putExtra("story", story); // Pass the story object
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Story story);
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView storyTitle;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            storyTitle = itemView.findViewById(R.id.notification_title);
        }
    }
}
