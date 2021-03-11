package com.example.uploadphoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.autofill.OnClickAction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class MainActivity<imageUri, uploadTask> extends AppCompatActivity {
    Button button;
    ListView li;
    ArrayList<User> arr = new ArrayList<User>();
    FirebaseDatabase fire;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        li = findViewById(R.id.list);
        button = (Button) findViewById(R.id.buttonLoadPicture);
        fire = FirebaseDatabase.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            storageRef.child("images/" + imageUri.getLastPathSegment()).putFile(imageUri).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Log.e("kata", "onFailure: " + exception.toString());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    DatabaseReference fire_ref = fire.getReference();
                    fire_ref.push().setValue(new User("images/" + imageUri.getLastPathSegment()));
                    DatabaseReference newref = fire.getReference();
                    newref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            arr = new ArrayList<User>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                arr.add(dataSnapshot.getValue(User.class));
                            }
                            CustomAdapter ad = new CustomAdapter(MainActivity.this, arr);
                            li.setAdapter(ad);
                            ad.notifyDataSetChanged();


                        }
//hulalallalalala
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                    Toast.makeText(MainActivity.this, "great", Toast.LENGTH_LONG).show();
                }
            });
            ;

        }
    }

// Register observers to listen for when the download is done or if it fails
}