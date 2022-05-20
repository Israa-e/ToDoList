package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    TextView login, singUp;
    FragmentManager mf = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        singUp = findViewById(R.id.signUp);
        Intent intent = getIntent();
        String log = "login";
        String is = intent.getStringExtra("login");

        FragmentTransaction ft = mf.beginTransaction();
        if (is.equals(log)) {
            ft.replace(R.id.fvm, new LoginFragment()).commit();
        } else {
            ft.replace(R.id.fvm, new SignUpFragment()).commit();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = mf.beginTransaction();
                ft.replace(R.id.fvm, new LoginFragment()).commit();


            }
        });
        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = mf.beginTransaction();
                ft.replace(R.id.fvm, new SignUpFragment()).commit();

            }
        });

    }

    public void _FindId() {
        TextView login = findViewById(R.id.login);
        TextView singUp = findViewById(R.id.signUp);
    }

    public void _Listener() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = mf.beginTransaction();
                ft.replace(R.id.fvm, new LoginFragment()).commit();

            }
        });
        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = mf.beginTransaction();
                ft.replace(R.id.fvm, new SignUpFragment()).commit();

            }
        });

    }
}