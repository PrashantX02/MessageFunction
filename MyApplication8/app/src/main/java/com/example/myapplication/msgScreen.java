package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class msgScreen extends AppCompatActivity {

    String imgUri,Name;
    CircleImageView Rimage,sendBtn;
    EditText msg;
    TextView Rname;
    String userId,recieverId;

    FirebaseAuth auth;

    public static String senderImg;
    public static String reciverImg;

    String SenderRoom,reciverRoom;

    RecyclerView msgRecyclerView;

    DatabaseReference chatReference;
    DatabaseReference userReference;

    FirebaseDatabase database;

    ArrayList<MsgModel> msgModelArrayList;

    messageAdapterRecyclerView adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_screen);

        auth  = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
      //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Intent intent  = getIntent();
        imgUri = intent.getStringExtra("img");
        Name = intent.getStringExtra("nme");
        recieverId = intent.getStringExtra("id");

        Rimage = findViewById(R.id.reciever_img);
        Rname = findViewById(R.id.reciever_name);

        msgModelArrayList = new ArrayList<>();

        msg = findViewById(R.id.msg);




        msgRecyclerView = findViewById(R.id.msgAdapt);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        msgRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new messageAdapterRecyclerView(msgScreen.this,msgModelArrayList);
        msgRecyclerView.setAdapter(adapter);





        Picasso.get().load(imgUri).into(Rimage);
        Rname.setText(Name);

        sendBtn = findViewById(R.id.sendButton);

        userId  = auth.getUid();

        SenderRoom = userId+recieverId;
        reciverRoom = recieverId+userId;


        chatReference = FirebaseDatabase.getInstance().getReference().child("chats").child(SenderRoom).child("msg");
        userReference = FirebaseDatabase.getInstance().getReference().child("user").child(auth.getUid());

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msgModelArrayList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    MsgModel msgModel = snapshot1.getValue(MsgModel.class);
                    if (msgModel != null) {
                        msgModelArrayList.add(msgModel);
                    }
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImg = snapshot.child("imageUri").getValue().toString();
                reciverImg = imgUri;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messageText = msg.getText().toString();
                msg.setText("");
                if (!TextUtils.isEmpty(messageText)) {
                    sendMessage(messageText);
                }
            }
        });
    }

    private void sendMessage(String message) {
        Date date = new Date();
        MsgModel msgModel = new MsgModel(message, date.getTime(), userId);

        database = FirebaseDatabase.getInstance();
       database.getReference().child("chats").child(SenderRoom).child("msg").push().setValue(msgModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                           database.getReference().child("chats")
                                   .child(reciverRoom)
                                   .child("msg")
                                   .push().setValue(msgModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {

                                       }
                                   });
                        } else {
                            Toast.makeText(msgScreen.this, "Failed to send message", Toast.LENGTH_LONG).show();
                            Log.e("SendMsg", "Failed to send message: " + task.getException());
                        }
                    }
                });
    }
}