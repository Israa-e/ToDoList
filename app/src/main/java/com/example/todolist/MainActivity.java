package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn_login, btn_logup;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _FindId();
        _Listener();
    }

    public void _FindId() {
        btn_login = findViewById(R.id.btn_login);
        btn_logup = findViewById(R.id.btn_singin);
    }

    public void _Listener() {
        btn_login.setOnClickListener(view -> {
            intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("login", "login");
            startActivity(intent);
            finish();
        });
        btn_logup.setOnClickListener(view -> {
            intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra("login", "singUp");
            startActivity(intent);
            finish();
        });


    }

}