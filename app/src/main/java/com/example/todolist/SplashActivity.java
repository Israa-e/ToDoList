package com.example.todolist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.logging.Handler;

public class SplashActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        textView = findViewById(R.id.text);
        textView.animate().translationY(-200).setDuration(2710).setStartDelay(20);
        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(5 * 994);
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                }
            }
        };
        background.start();
    }
}