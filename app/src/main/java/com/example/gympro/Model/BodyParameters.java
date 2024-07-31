package com.example.gympro.Model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.gympro.activities.MainActivity;
import com.example.gympro.databinding.ActivityBodyParametersBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BodyParameters extends AppCompatActivity {

    private ActivityBodyParametersBinding binding;

    private NumberPicker npWeight, npHeight, npAge;

    private SharedPreferences sharedPreferences;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBodyParametersBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);

        binding.npWeight.setMinValue(40);
        binding.npWeight.setMaxValue(150);
        binding.npWeight.setWrapSelectorWheel(false);

        binding.npHeight.setMinValue(140);
        binding.npHeight.setMaxValue(220);
        binding.npHeight.setWrapSelectorWheel(false);

        binding.npAge.setMinValue(10);
        binding.npAge.setMaxValue(100);
        binding.npAge.setWrapSelectorWheel(false);

        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBodyParameters();
            }
        });

    }

    private void saveBodyParameters() {
        int weight = binding.npWeight.getValue();
        int height = binding.npHeight.getValue();
        int age = binding.npAge.getValue();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Retrieve the current user's profile
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        UserProfile userProfile = snapshot.getValue(UserProfile.class);

                        // Update the user's body parameters
                        if (userProfile != null) {
                            userProfile.setWeight(weight);
                            userProfile.setHeight(height);
                            userProfile.setAge(age);

                            // Save the updated profile back to the database
                            databaseReference.child(userId).setValue(userProfile)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            // Save values in SharedPreferences (optional)
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putInt("weight", weight);
                                            editor.putInt("height", height);
                                            editor.putInt("age", age);
                                            editor.apply();

                                            Toast.makeText(BodyParameters.this, "Body parameters updated", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(BodyParameters.this, MainActivity.class));
                                        } else {
                                            Toast.makeText(BodyParameters.this, "Failed to update body parameters", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(BodyParameters.this, "User profile not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(BodyParameters.this, "User profile not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(BodyParameters.this, "Failed to fetch user profile", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}