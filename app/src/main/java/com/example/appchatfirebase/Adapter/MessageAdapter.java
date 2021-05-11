package com.example.appchatfirebase.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchatfirebase.Model.Chat;
import com.example.appchatfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mcontext;
    private List<Chat> mChat;
    private String imageurl;
    FirebaseUser fUser;

    public MessageAdapter(Context mcontext, List<Chat> mChat, String imageurl) {
        this.mcontext = mcontext;
        this.mChat = mChat;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_right, parent, false);
        } else {
            view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_left, parent, false);
        }
        return new MessageAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = mChat.get(position);

        holder.show_message.setText(chat.getMessage());
        if(imageurl.equals("default")){
            holder.profile_image1.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(mcontext).load(imageurl).into(holder.profile_image1);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public CircleImageView profile_image1;
        public ViewHolder(View view){
            super(view);
            show_message = view.findViewById(R.id.show_message);
            profile_image1 = view.findViewById(R.id.profile_image);

        }
    }

    @Override
    public int getItemViewType(int position) {
            if (mChat.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                return MSG_TYPE_RIGHT;
            } else {
                return MSG_TYPE_LEFT;
            }
}
}
