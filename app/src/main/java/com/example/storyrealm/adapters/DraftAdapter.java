package com.example.storyrealm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.storyrealm.models.Chapter;

import java.util.ArrayList;

public class DraftAdapter extends ArrayAdapter<Chapter> {

    private final Context context;
    private final ArrayList<Chapter> chapters;

    public DraftAdapter(@NonNull Context context, ArrayList<Chapter> chapters) {
        super(context, 0, chapters);
        this.context = context;
        this.chapters = chapters;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        Chapter chapter = chapters.get(position);
        textView.setText(chapter.getTitle());

        return convertView;
    }
}
