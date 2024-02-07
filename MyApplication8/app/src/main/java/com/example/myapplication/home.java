package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class home extends AppCompatActivity {

    FirebaseAuth auth;
    ArrayList<Drivers_user> userArrayList;
    RecyclerView recyclerView;
    RecyclerViewAdapterUser recyclerViewAdapterUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();

        Button logOut = findViewById(R.id.logout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(home.this,MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });


            userArrayList = new ArrayList<>();
            recyclerView = findViewById(R.id.recyclerView1);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Drivers_user user = snapshot1.getValue(Drivers_user.class);
                        userArrayList.add(user);
                    }
                    Log.d("Firebase", "Data getting complete");
                    recyclerViewAdapterUser.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(home.this));
            recyclerViewAdapterUser = new RecyclerViewAdapterUser(home.this, userArrayList);
            recyclerView.setAdapter(recyclerViewAdapterUser);


            if (auth.getCurrentUser() == null) {
                Intent intent = new Intent(home.this, MainActivity2.class);
                startActivity(intent);
                finish();
            }
        }
}