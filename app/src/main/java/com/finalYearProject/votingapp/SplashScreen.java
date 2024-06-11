package com.finalYearProject.votingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.finalYearProject.votingapp.activities.HomeActivity;
import com.finalYearProject.votingapp.activities.LoginActivity;

public class SplashScreen extends AppCompatActivity {
    public static final String PREFERENCES="prefkey";
    SharedPreferences sharedPreferences;
    public static final String IsLogIn="islogin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.smain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences=getApplicationContext().getSharedPreferences(PREFERENCES,MODE_PRIVATE);
        boolean bol=sharedPreferences.getBoolean(IsLogIn,false);
        new Handler().postDelayed(()-> {
            if(bol){
                startActivity(new Intent(SplashScreen.this, HomeActivity.class));
            }else {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                finish();
            }
        },3000);
    }
}