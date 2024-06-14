package com.example.gympro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.gympro.databinding.ActivityLoginBinding;
import com.example.gympro.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);


        binding.textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });

        auth = FirebaseAuth.getInstance();
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.editTextEmail.getText().toString();
                String pass  =  binding.editTextPassword.getText().toString();

                if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                   if(!pass.isEmpty()){
                       auth.signInWithEmailAndPassword(email,pass)
                               .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                           @Override
                           public void onSuccess(AuthResult authResult) {
                               Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                               startActivity(new Intent(LoginActivity.this,MainActivity.class));
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
                                   }
                               });
                   }   else{
                       binding.editTextPassword.setError("Password cannot be Empty");
                   }
                }else if(email.isEmpty()){
                    binding.editTextEmail.setError("Email cannot be empty");
                }else{
                    binding.editTextEmail.setError("Please enter valid email");
                }
            }
        });

    }
}