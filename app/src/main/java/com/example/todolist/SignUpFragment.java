package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpFragment extends Fragment {


    public SignUpFragment() {
    }

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        TextInputEditText fullName = v.findViewById(R.id.fullName);
        TextInputEditText phone = v.findViewById(R.id.phone);
        TextInputEditText email = v.findViewById(R.id.email);
        TextInputEditText passwordSingUp = v.findViewById(R.id.passwordSingUp);
        AppCompatButton login = v.findViewById(R.id.login2);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullNameText = fullName.getText().toString();
                String phoneText = phone.getText().toString();
                String emailText = email.getText().toString();
                String passwordSingUpText = passwordSingUp.getText().toString();
                if (fullNameText.isEmpty()) {
                    Toast.makeText(getContext(), "Full Name Cant Be Empty !", Toast.LENGTH_SHORT).show();
                    fullName.setError("Can't be empty");
                } else if (phoneText.isEmpty()) {
                    Toast.makeText(getContext(), "Phone Cant Be Empty !", Toast.LENGTH_SHORT).show();
                    phone.setError("Can't be empty");
                } else if (emailText.isEmpty()) {
                    Toast.makeText(getContext(), "Email Cant Be Empty !", Toast.LENGTH_SHORT).show();
                    email.setError("Can't be empty");
                } else if (passwordSingUpText.isEmpty()) {
                    Toast.makeText(getContext(), "Password Cant Be Empty !", Toast.LENGTH_SHORT).show();
                    passwordSingUp.setError("Can't be empty");
                } else if (passwordSingUpText.length() < 6) {
                    Toast.makeText(getContext(), "Password Must Be More Than 6 Numbers", Toast.LENGTH_SHORT).show();
                    passwordSingUp.setError("Can't be less than 6 numbers");
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(emailText, passwordSingUpText).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        if (user != null) {

                                            String Uid = user.getUid();
                                            HashMap<String, String> data = new HashMap<>();
                                            data.put("uid", Uid);
                                            data.put("fullName", fullNameText);
                                            data.put("phone", phoneText);
                                            data.put("email", emailText);

                                            firebaseFirestore.collection("users").add(data).
                                                    addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                            if (task.isSuccessful()) {
                                                                System.out.println("Created Account");
                                                            } else {
                                                                System.out.println("Error");
                                                            }
                                                        }
                                                    });
                                        }

//                                        data.put("password",passwordSingUpText);
                                        Toast.makeText(getContext(), "Created Account", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getContext(), HomePageActivity.class));
                                        getActivity().getFragmentManager().popBackStack();

                                    } else {
                                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

//                    Intent intent =new Intent(getContext(),HomePageActivity.class);
//                    intent.putExtra("fullName",fullNameText);
//                    intent.putExtra("phone",phoneText);
//                    intent.putExtra("email",emailText);
//                    intent.putExtra("password",passwordSingUpText);
//                    startActivity(intent);

                }


            }
        });
        return v;
    }
}