package com.example.healthwise;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class StoryContentListAdapter extends ArrayAdapter {



    private Activity mContext;
    List<String> storiesList;

    public StoryContentListAdapter(Activity mContext, List<String> storiesList)  {
        super(mContext, R.layout.stories_listed, storiesList);
        this.mContext = mContext;
        this.storiesList = storiesList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.stories_listed, null, true);

        TextView tvStories = listItemView.findViewById(R.id.tvStories);


        String stories = storiesList.get(position);

        tvStories.setText(stories.toString());

        return listItemView;
        // super.getView(position, convertView, parent);
    }

}
