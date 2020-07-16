package com.rajlee.firebaseauth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth mAuth;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTexEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressbar);


        findViewById(R.id.textView).setOnClickListener(this);
        findViewById(R.id.buttonLogIn).setOnClickListener(this);
    }

    private void userLogin(){

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please Enter a valid Email");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textView:
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                break;

            case R.id.buttonLogIn:
                userLogin();
                break;
        }
    }
}