package com.rajlee.firebaseauth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    FirebaseFirestore fStore;
    private static final int CHOOSE_IMAGE = 101;
    ImageView imageView;
    EditText mFullName, mPhone;
    private FirebaseDatabase usersReference;

    Uri uriProfileImage;
    ProgressBar progressBar;
    String profileImageUrl;
    FirebaseAuth mAuth;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        usersReference = FirebaseDatabase.getInstance();

        mFullName = findViewById(R.id.etPersonName);
        mPhone = findViewById(R.id.etNumber);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressbar);


        String fullName = mFullName.getText().toString();


        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showImageChooser();

            }
        });

        userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        loadUserInformation();
        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveUserInformation();
                saveUserDetails();
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });
    }

    private void saveUserDetails() {
        DocumentReference documentReference = fStore.collection("Users").document(userID);
        Map<String, String> user = new HashMap<>();
        String fullName = mFullName.getText().toString();
        String number = mPhone.getText().toString();
        user.put("fName", fullName);
        user.put("phone", number);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "Data is Store Successfully for " + userID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure" + e.toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }


    private void loadUserInformation() {

        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
        if(user.getPhotoUrl() != null){

            Glide.with(this)
                    .load(user.getPhotoUrl().toString())
                    .into(imageView);
        }
//        if(user.getDisplayName() != null){
//            editText.setText(user.getDisplayName());
//        }

        }
        assert user != null;
        String display = user.getDisplayName();


    }

    private void saveUserInformation() {

        usersReference.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                   Toast.makeText(ProfileActivity.this, "New Child is Created", Toast.LENGTH_SHORT).show();
                }
                else{
                    usersReference.getReference().child("Users");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        String  displayName = mFullName.getText().toString();
        if(displayName.isEmpty()){
            mFullName.setError("Name is required");
            mFullName.requestFocus();
            return;
        }
        FirebaseUser user = mAuth.getCurrentUser();
        String name = mFullName.toString();

        if(user != null && profileImageUrl != null && !name.equals("Your Name")){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();
            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ProfileActivity.this, "Profile is Updated",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
        }else{
            Toast.makeText(ProfileActivity.this, "Please Provide Valid informations", Toast.LENGTH_SHORT).show();
            mFullName.setError("Give Valid Name");
            mFullName.requestFocus();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){

            uriProfileImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/"+ userID + ".jpg");

        if(uriProfileImage != null){
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);

                            profileImageUrl = taskSnapshot.getStorage().getDownloadUrl().toString();
                        }
                    })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }
}