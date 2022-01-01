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

//adapter for the categories array list
public class ListAdapter extends ArrayAdapter {

    Activity mContext;
    List<String> storiesList;

    public ListAdapter(Activity mContext, List<String> storiesList)  {
        super(mContext, R.layout.categories_listed, storiesList);
        this.mContext = mContext;
        this.storiesList = storiesList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.categories_listed, null, true);

        TextView tvStories = listItemView.findViewById(R.id.tvCategories);

        String stories = storiesList.get(position);

        tvStories.setText(stories);

        return listItemView;
    }

}
