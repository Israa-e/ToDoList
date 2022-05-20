package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        TextInputEditText username = v.findViewById(R.id.username);
        TextInputEditText passwordLogin = v.findViewById(R.id.passwordLogin);
        AppCompatButton login = v.findViewById(R.id.login1);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameText = username.getText().toString();
                String passwordText = passwordLogin.getText().toString();

                if (usernameText.isEmpty()) {
                    Toast.makeText(getContext(), "Username Cant Be Empty !", Toast.LENGTH_SHORT).show();
                    username.setError("Can't be empty");

                } else if (passwordText.isEmpty()) {
                    Toast.makeText(getContext(), "Password Cant Be Empty !", Toast.LENGTH_SHORT).show();
                    passwordLogin.setError("Can't be empty");
                } else if (passwordText.length() < 6) {
                    Toast.makeText(getContext(), "Password Must Be More Than 6 Numbers", Toast.LENGTH_SHORT).show();
                    passwordLogin.setError("Can't be less than 6 numbers");
                } else {
                    firebaseAuth.signInWithEmailAndPassword(usernameText, passwordText).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                        Log.d("TAG", "onComplete: successful");
                                        startActivity(new Intent(getContext(), HomePageActivity.class));
                                        getActivity().getFragmentManager().popBackStack();
                                    } else {
                                        Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                        Log.d("TAG", "onComplete: Failed");
                                    }
                                }
                            });
//                    Intent intent =new Intent(getContext(),HomePageActivity.class);
//                    intent.putExtra("username",usernameText);
//                    intent.putExtra("password",passwordText);
//                    startActivity(intent);

                }
            }
        });

        return v;
    }
}