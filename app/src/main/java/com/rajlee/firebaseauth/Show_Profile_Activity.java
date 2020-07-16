package com.rajlee.firebaseauth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Show_Profile_Activity extends AppCompatActivity {

    TextView name, number;
    Button btlogout;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_show);

        name = findViewById(R.id.Name);
        number = findViewById(R.id.Number);
        btlogout = findViewById(R.id.logout);

        btlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();


        final DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                assert documentSnapshot != null;
                name.setText(documentSnapshot.getString("fName"));

                number.setText(documentSnapshot.getString("phone"));
            }


        });

    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}