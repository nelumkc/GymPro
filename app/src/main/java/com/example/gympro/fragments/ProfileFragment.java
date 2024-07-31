package com.example.gympro.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gympro.Model.UserProfile;
import com.example.gympro.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private DatabaseReference reference;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize binding
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize Firebase auth and database reference
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");

        // Check if the user is logged in
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getActivity(), "User not logged in", Toast.LENGTH_SHORT).show();
            return view;
        }

        showUserData();

        // Debugging Toast
        Toast.makeText(getActivity(), "Fragment Loaded", Toast.LENGTH_SHORT).show();

        return view;
    }

    private void showUserData() {
        String userId = auth.getCurrentUser().getUid();

        // Debugging Toast
        Toast.makeText(getActivity(), "Fetching data for user: " + userId, Toast.LENGTH_SHORT).show();

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);

                if (userProfile != null) {
                    binding.profileName.setText(userProfile.getName());
                    binding.profileEmail.setText(userProfile.getEmail());
                    binding.postsNumber.setText(String.valueOf(userProfile.getWeight()));
                    binding.totalNo.setText(String.valueOf(userProfile.getHeight()));
                    binding.followingNo.setText(String.valueOf(userProfile.getAge()));
                    binding.titleName.setText(userProfile.getName());

                } else {
                    Toast.makeText(getActivity(), "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
