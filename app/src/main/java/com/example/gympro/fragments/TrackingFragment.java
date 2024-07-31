package com.example.gympro.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gympro.R;
import com.squareup.picasso.Picasso;

public class TrackingFragment extends Fragment {

    private TextView exerciseName;
    private ImageView exerciseImage;
    private TextView exerciseInstructions;
    private TextView exerciseEquipment;
    private TextView exerciseDifficulty;

    private TextView exerciseMuscle;

    private Button startWorkout;

    TextView mainTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracking, container, false);

        exerciseName = view.findViewById(R.id.workout_name);
        exerciseImage = view.findViewById(R.id.workout_image);
        exerciseInstructions = view.findViewById(R.id.workout_description);
        exerciseEquipment = view.findViewById(R.id.workout_equipment);
        exerciseDifficulty = view.findViewById(R.id.workout_difficulty);
        exerciseMuscle = view.findViewById(R.id.workout_muscle);
        startWorkout = view.findViewById(R.id.start_workout_button);


        Bundle args = getArguments();
        if (args != null) {
            exerciseName.setText(args.getString("name"));
            exerciseInstructions.setText(args.getString("instructions"));
            exerciseEquipment.setText(args.getString("equipment"));
            exerciseDifficulty.setText(args.getString("difficulty"));
            exerciseMuscle.setText(args.getString("muscle"));
            //mainTitle.setText("WOrkout Detials");

            String imageUrl = args.getString("image");
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.get().load(imageUrl).into(exerciseImage);
            }
        }

        startWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerDialogFragment timerDialogFragment = new TimerDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("workoutName", exerciseName.getText().toString());
                timerDialogFragment.setArguments(bundle);

                timerDialogFragment.show(getParentFragmentManager(), "timerDialog");
            }
        });

        return view;
    }
}