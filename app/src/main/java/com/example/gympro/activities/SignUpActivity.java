package com.example.gympro.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gympro.Model.UserProfile;
import com.example.gympro.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private FirebaseAuth auth;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.editTextEmail.getText().toString().trim();
                String pass = binding.editTextPassword.getText().toString().trim();
                String name = binding.editTextName.getText().toString().trim();
                reference = database.getReference("users");


                if(email.isEmpty()){
                    binding.editTextEmail.setError("Email cannot be empty");
                }
                if(pass.isEmpty()){
                    binding.editTextPassword.setError("Password cannot be empty");
                }else{
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String userId = auth.getCurrentUser().getUid();
                                UserProfile userProfile = new UserProfile(userId,name,email,0,0,0);
                                reference.child(userId).setValue(userProfile);

                                Toast.makeText(SignUpActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            }else{
                                Toast.makeText(SignUpActivity.this, "Sign up Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }
}