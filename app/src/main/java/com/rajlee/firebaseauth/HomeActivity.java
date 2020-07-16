package com.rajlee.firebaseauth;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class HomeActivity extends AppCompatActivity {

  Button next, previous, demo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        next = findViewById(R.id.buttonNext);
        previous = findViewById(R.id.buttonPrevious);
        demo = findViewById(R.id.buttonDemo);


        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtubeView);
        getLifecycle().addObserver(youTubePlayerView);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "Please Make Payments for Further Involments"
                , Toast.LENGTH_SHORT).show();
//                Intent home_final = new Intent(HomeActivity.this, HomeActivityFinal.class);
//                startActivity(home_final);
            }
        });

        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent demo = new Intent(HomeActivity.this, DemoActivity.class);
//                Toast.makeText(HomeActivity.this, "Demo will be Activated on 10th July, Stay Connected",
//                        Toast.LENGTH_SHORT).show();
                startActivity(demo);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Toast.makeText(this,"Item1 is selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2:
                Toast.makeText(this,"Item12 is selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item3:
                Toast.makeText(this,"Item3 is selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sub_item1:
                Toast.makeText(this,"Sub-item1 is selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sub_item2:
                Toast.makeText(this,"Sub-Item 2 is selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intToMain);
                Toast.makeText(this, "You are Successfully Logout", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.editProfile:
                Intent p = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(p);
                Toast.makeText(this, "You are Successfully On Profile Edit", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.showProfile:
                Intent pa = new Intent(HomeActivity.this, Show_Profile_Activity.class);
                startActivity(pa);
                Toast.makeText(this, "You are Successfully On Profile View", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}