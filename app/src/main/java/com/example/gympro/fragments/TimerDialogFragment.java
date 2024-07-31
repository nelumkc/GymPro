package com.example.gympro.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gympro.Model.Workout;
import com.example.gympro.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimerDialogFragment extends DialogFragment {

    private TextView timerTextView;
    private Button pauseButton, stopButton;
    private long startTime = 0L;
    private long pauseTime = 0L;
    private boolean isPaused = false;
    private Handler handler = new Handler();
    private long elapsedTimeWhenPaused = 0L;

    private String userId;
    private String workoutName;

    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timer_dialog, container, false);

        timerTextView = view.findViewById(R.id.timer_text);
        pauseButton = view.findViewById(R.id.pause_button);
        stopButton = view.findViewById(R.id.stop_button);

        if (getArguments() != null) {
            workoutName = getArguments().getString("workoutName", "Workout");
        }

        startWorkout();

        pauseButton.setOnClickListener(v -> {
            if (!isPaused) {
                pauseWorkout();
                pauseButton.setText("Resume");
            } else {
                resumeWorkout();
                pauseButton.setText("Pause");
            }
        });

        stopButton.setOnClickListener(v -> stopWorkout());

        return view;
    }

    private void startWorkout() {
        startTime = System.currentTimeMillis();
        handler.postDelayed(updateTimerThread, 0);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;

            if (isPaused) {
                elapsedTime = elapsedTimeWhenPaused;
            } else {
                elapsedTimeWhenPaused = elapsedTime;
            }

            int seconds = (int) (elapsedTime / 1000) % 60;
            int minutes = (int) ((elapsedTime / (1000 * 60)) % 60);
            int hours = (int) ((elapsedTime / (1000 * 60 * 60)) % 24);

            timerTextView.setText(String.format("%d:%02d:%02d", hours, minutes, seconds));
            handler.postDelayed(this, 1000);
        }
    };

    private void pauseWorkout() {
        elapsedTimeWhenPaused = System.currentTimeMillis() - startTime;
        handler.removeCallbacks(updateTimerThread);
        isPaused = true;
    }

    private void resumeWorkout() {
        startTime = System.currentTimeMillis() - elapsedTimeWhenPaused;
        handler.postDelayed(updateTimerThread, 0);
        isPaused = false;
    }

    private void stopWorkout() {
        handler.removeCallbacks(updateTimerThread);
        recordWorkoutTimeToFirebase(elapsedTimeWhenPaused);
        dismiss();
    }

    private void recordWorkoutTimeToFirebase(long totalWorkoutTime) {
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("workouts").child(userId);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Workout workout = new Workout(date, totalWorkoutTime / 1000, workoutName); // Store time in seconds
        reference.push().setValue(workout);
    }
}