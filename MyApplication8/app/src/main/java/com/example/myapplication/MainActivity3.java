package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class MainActivity3 extends AppCompatActivity {

    EditText passcode,mail,name;

    private int code= 1001;

    private Uri uri;

    FirebaseAuth auth;

    String imageUri;
    Button button;

    ImageView user_image;


   // FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mail = findViewById(R.id.getMail);
        passcode = findViewById(R.id.getPassword);

        name  = findViewById(R.id.name);
        button = findViewById(R.id.Register_Now);


        auth = FirebaseAuth.getInstance();
//        database = FirebaseDatabase.getInstance();
//        storage = FirebaseStorage.getInstance();

        user_image = findViewById(R.id.profile_image);

        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,code);
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  m = mail.getText().toString();
                String  p = passcode.getText().toString();
                String  n = name.getText().toString();
                String  s = "hey i'm using this application";

                // Inside the onClickListener of the button
                auth.createUserWithEmailAndPassword(m, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id = task.getResult().getUser().getUid();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user").child(id);
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Upload").child(id);

                            // Upload image to Firebase Storage
                            storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        // Get the download URL for the uploaded image
                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri downloadUri) {
                                                String imageUri = downloadUri.toString(); // Capture the image URL

                                                // Create the Drivers_user object with the image URI
                                                Drivers_user user = new Drivers_user(id, n, m, p, imageUri, s);

                                                // Save user data to Realtime Database
                                                reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            // Data added successfully
                                                            Intent intent = new Intent(MainActivity3.this,MainActivity2.class);
                                                            startActivity(intent);
                                                            finish();

                                                            Log.d("Firebase", "Data added successfully under user/" + id);
                                                            // Add your success logic here
                                                        } else {
                                                            // Data addition failed
                                                            Log.e("Firebase", "Failed to add data: " + task.getException());
                                                            // Add your failure logic here
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        // Image upload failed
                                        Log.e("Firebase", "Image upload failed: " + task.getException());
                                        Toast.makeText(MainActivity3.this, "Error uploading image", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            // User creation failed
                            Log.e("Firebase", "User creation failed: " + task.getException());
                            Toast.makeText(MainActivity3.this, "User creation failed", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // General failure for user creation or other unexpected errors
                        Log.e("Firebase", "Failed: " + e.getMessage());
                        Toast.makeText(MainActivity3.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(code == requestCode){
                uri = data.getData();
                Bitmap bitmap = uriToBitMap(uri);
                user_image.setImageBitmap(bitmap);

            }
        }
    }

    private Bitmap uriToBitMap(Uri uri){
        Bitmap bitmap = null;

        try{
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.O_MR1){
                ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(),uri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }else{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return bitmap;
    }
}