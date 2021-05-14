package com.example.appchatfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.appchatfirebase.Adapter.MessageAdapter;
import com.example.appchatfirebase.Model.Chat;
import com.example.appchatfirebase.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageMainActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;
    ImageButton bnt_send;
    EditText text_send;
    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v ->startActivity(new Intent(MessageMainActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));

        recyclerView = findViewById(R.id.rc_mess);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);

        circleImageView = findViewById(R.id.image_user);
        username = findViewById(R.id.txtUserName);
        intent = getIntent();
        String userid = intent.getStringExtra("userid");
        bnt_send = findViewById(R.id.image_send);
        text_send = findViewById(R.id.edt_message);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        bnt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mes = text_send.getText().toString();
                if (!mes.equals("")){
                    sendMessage(firebaseUser.getUid(),userid,mes);
                }else {
                    Toast.makeText(MessageMainActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }

                text_send.setText("");
            }
        });



        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());

                if (user.getImageURL().equals("default")){
                    circleImageView.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Glide.with(MessageMainActivity.this).load(user.getImageURL()).into(circleImageView);
                }
                readMessage(firebaseUser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        reference.child("Messages").push().setValue(hashMap);
    }
    private void readMessage(final String myid,final String userid,final String imageurl){
        mchat = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mchat.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat != null) {
                        if (myid.equals(chat.getReceiver()) && userid.equals(chat.getSender()) ||
                                myid.equals(chat.getSender()) && userid.equals(chat.getReceiver())) {
                            mchat.add(chat);
                        }

                        messageAdapter = new MessageAdapter(MessageMainActivity.this, mchat, imageurl);
                        recyclerView.setAdapter(messageAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("status").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status",status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}