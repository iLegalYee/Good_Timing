package edu.fsu.cs.goodtiming.Utils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import edu.fsu.cs.goodtiming.R;

public class SplashActivity extends AppCompatActivity {

    // code for the duration of the splash screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, WalkthroughActivity.class));
                finish();
            }
        },4000);

    }
}