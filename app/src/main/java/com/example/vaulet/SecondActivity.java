package com.example.vaulet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SecondActivity extends AppCompatActivity {
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    static final int REQUEST_IMAGE_GET = 1;
//    private FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            selectImage();


        });
        TextView exit=findViewById(R.id.exit),signout=findViewById(R.id.sign_out);
        exit.setOnClickListener(v -> System.exit(0));
    signout.setOnClickListener(v -> mAuth.signOut());
    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Bitmap thumbnail = data.getExtras().getParcelable("data");
            Uri fullPhotoUri = data.getData();
            // Do work with photo saved at fullPhotoUri
            StorageReference riversRef = mStorageRef.child("images/");
            riversRef.putFile(fullPhotoUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get a URL to the uploaded content
                        riversRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            Uri down=uri;
                            Toast.makeText(SecondActivity.this,"File uploaded download url-"+down.toString(),Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(exception -> {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(this,"Error try again",Toast.LENGTH_SHORT).show();
                    });
        }
    }

}
