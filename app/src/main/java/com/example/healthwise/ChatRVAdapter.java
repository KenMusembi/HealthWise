package com.example.healthwise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
RecyclerView for the Chat feature
RecyclerView makes it easy to efficiently display large sets of data.
 */
public class ChatRVAdapter extends RecyclerView.Adapter {

    //provide a reference to the type of views used, i.e arraylist and context
    private ArrayList<ChatsModel> chatsModelArrayList;
    private Context context;

    //Constructor for the ChatRVAdapter class, loaded with arraylist and context
    public ChatRVAdapter(ArrayList<ChatsModel> chatsModelArrayList, Context context) {
        this.chatsModelArrayList = chatsModelArrayList;
        this.context = context;
    }

    //The ViewHolder is a wrapper around a View that contains the layout for an individual item in the list.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view;
        // below code is to switch our layout type along with view holder.
       switch(viewType){
           //case 0 is when it is the user
           case 0:
               view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg_rv_item, parent, false);
               return new UserViewHolder(view);
           //case 1 is when it is the bot
           case 1:
               view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_msg_rv_item, parent, false);
               return new BotViewHolder(view);
       }
       //returning null because we only have two views of interest
       return null;
    }

    //here we bind the content to the specific view
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //we use chats model, which has sender parameter we use to switch the layout
        ChatsModel chatsModel = chatsModelArrayList.get(position);
        switch(chatsModel.getSender()){
            //if sender is user, switch the view to the user Text view, userTv
            case "user":
                ((UserViewHolder)holder).userTv.setText(chatsModel.getMessage());
                break;
            //if the sender is the bot, switch the view to the bot message view, botMsgTv
            case "bot":
                ((BotViewHolder)holder).botMsgTV.setText(chatsModel.getMessage());
                break;
        }
    }

    @Override
    public int getItemCount() {
        /*
        this method returns the number of items on the chats array list
        to determine when there are no more items that can be displayed.
        */
        return chatsModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //here we get the type of view, whether user or bot, we default to -1 if neither
        switch(chatsModelArrayList.get(position).getSender()){
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    //the user view holder
    public static class UserViewHolder extends RecyclerView.ViewHolder{
        TextView userTv;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userTv = itemView.findViewById(R.id.idTVUser);
        }
    }

    //the bot view holder
    public static class BotViewHolder extends RecyclerView.ViewHolder{
        TextView botMsgTV;
        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            botMsgTV = itemView.findViewById(R.id.idTVBot);
        }
    }
}
