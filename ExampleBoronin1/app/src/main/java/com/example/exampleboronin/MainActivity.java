package com.example.exampleboronin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button b_profile,b_timer,b_logOut,b_reg;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            Toast.makeText(getApplicationContext(), "Перейдите в раздел Настройки", Toast.LENGTH_SHORT).show();
        b_profile = (Button) findViewById(R.id.button_read);
        b_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });

        b_timer = (Button) findViewById(R.id.button_timer);
        b_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimer();
            }
        });

        b_reg = (Button) findViewById(R.id.button_registration);
        b_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReg();
            }
        });
        b_logOut = (Button) findViewById(R.id.b_LogOut);
        b_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
            }
        });
            Intent i  = getIntent();
            String bName1 = i.getStringExtra("NewUsername");
            if(bName1 == null){
                b_profile.setVisibility(View.GONE);
            }
            else {
                b_profile.setVisibility(View.VISIBLE);
            }

    }
    public void openProfile(){
        Intent intent = new Intent(this,Profile.class);
        startActivity(intent);
    }
    public void openTimer(){
        Intent intent1 = new Intent(this,Timer.class);
        startActivity(intent1);
    }
    public void openReg(){
        Intent intent2 = new Intent(this, PersonalData.class);
        startActivity(intent2);
    }

}