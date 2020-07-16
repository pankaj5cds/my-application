package com.rajlee.firebaseauth;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity_quiz extends AppCompatActivity {
    Button start_button, about_button;
    EditText name_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz);

        start_button= findViewById(R.id.button);
        about_button= findViewById(R.id.button2);
        name_text= findViewById(R.id.editName);

                start_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View View) {
                        String name = name_text.getText().toString();
                        Intent intent=new Intent(getApplicationContext(),QuestionsActivity.class);
                        intent.putExtra("userName",name);
                        startActivity(intent);
                    }
                });

                about_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),DeveloperActivity.class);
                    startActivity(intent);
            }
        });
    }
}
