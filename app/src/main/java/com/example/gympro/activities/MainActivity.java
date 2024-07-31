package com.example.gympro.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.gympro.R;
import com.example.gympro.fragments.HomeFragment;
import com.example.gympro.fragments.ProfileFragment;
import com.example.gympro.fragments.WorkoutFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    WorkoutFragment workoutFragment = new WorkoutFragment();

    TextView mainTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        //title
        mainTitle = findViewById(R.id.mainTitle);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment).commit();
        mainTitle.setText("Home");

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment selectedFragment = null;
                String title = "";


                    if (item.getItemId() == R.id.bottom_home) {
                        selectedFragment = homeFragment;
                        title = "Home";
                    } else if (item.getItemId() == R.id.bottom_workout) {
                        selectedFragment = workoutFragment;
                        title = "Gym Equipment Guide";
                    } else if (item.getItemId() == R.id.bottom_diet) {
                        //selectedFragment = userRole.equals("PARENT") ? parentClassroomFragment : classroomFragment;
                        title = "Classroom";
                    } else if (item.getItemId() == R.id.bottom_profile) {
                        selectedFragment = profileFragment;
                        title = "Profile";
                    }


                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).commit();
                    mainTitle.setText(title);
                    return true;
                }
                return false;
            }
        });

    }





}