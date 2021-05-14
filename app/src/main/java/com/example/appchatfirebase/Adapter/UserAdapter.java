package com.example.appchatfirebase.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchatfirebase.MessageMainActivity;
import com.example.appchatfirebase.Model.User;
import com.example.appchatfirebase.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mcontext;
    private List<User> mUsers;
    private boolean ischat;

    public UserAdapter(Context mcontext, List<User> mUsers, boolean ischat) {
        this.mcontext = mcontext;
        this.mUsers = mUsers;
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.user_item,parent,false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.username.setText(user.getUsername());
        if(user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(mcontext).load(user.getImageURL()).into(holder.profile_image);
        }

        if (ischat){
            if (user.getStatus().equals("online")){
                holder.img_onl.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            }else {
                holder.img_onl.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }else {
            holder.img_onl.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, MessageMainActivity.class);
                intent.putExtra("userid",user.getId());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;
        public ImageView img_onl;
        public ImageView img_off;
        public ViewHolder(View view){
            super(view);
            username = view.findViewById(R.id.username);
            profile_image = view.findViewById(R.id.image_profile_user);
            img_onl = view.findViewById(R.id.img_onl);
            img_off = view.findViewById(R.id.img_off);

        }
    }
}
