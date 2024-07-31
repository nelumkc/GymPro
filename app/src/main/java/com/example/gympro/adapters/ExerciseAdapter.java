package com.example.gympro.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gympro.R;
import com.example.gympro.Model.Exercise;
import com.example.gympro.fragments.TrackingFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private static List<Exercise> exerciseList;

    private static Context context;

    public ExerciseAdapter(List<Exercise> exerciseList, Context context) {
        this.exerciseList = exerciseList;
        this.context = context;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        holder.exerciseName.setText(exercise.getName());
        holder.exerciseMuscle.setText(exercise.getMuscle());
        holder.exerciseEquipment.setText(exercise.getEquipment());
        holder.exerciseHashcode.setText(exercise.getHashcode());
        if(exercise.getImageUrl() !=null && !exercise.getImageUrl().isEmpty()){
            Picasso.get().load(exercise.getImageUrl()).into(holder.exerciseImage);
        }

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    public void setExercises(List<Exercise> filteredExercises) {
        this.exerciseList.clear();  // Clear the old list
        this.exerciseList.addAll(filteredExercises);  // Add the new data
        notifyDataSetChanged();
    }


    static class ExerciseViewHolder extends RecyclerView.ViewHolder {

        TextView exerciseName;
        TextView exerciseType;
        TextView exerciseMuscle;
        TextView exerciseEquipment;

        TextView exerciseHashcode;

        TextView exerciseInstructions;

        ImageView exerciseImage;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exercise_name);
            exerciseMuscle = itemView.findViewById(R.id.exercise_muscle);
            exerciseEquipment = itemView.findViewById(R.id.exercise_equipment);
            exerciseHashcode = itemView.findViewById(R.id.exercise_hashcode);
            exerciseImage = itemView.findViewById(R.id.exercise_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position =getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Exercise exercise = exerciseList.get(position);

                        Bundle bundle = new Bundle();
                        bundle.putString("name", exercise.getName());
                        bundle.putString("image", exercise.getImageUrl());
                        bundle.putString("instructions", exercise.getInstructions());
                        bundle.putString("equipment", exercise.getEquipment());
                        bundle.putString("difficulty", exercise.getDifficulty());
                        bundle.putString("muscle", exercise.getMuscle());

                        TrackingFragment trackingFragment = new TrackingFragment();
                        trackingFragment.setArguments(bundle);

                        FragmentActivity activity = (FragmentActivity) context;
                        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_container, trackingFragment); // Ensure 'frame_container' matches your layout
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                }
            });
        }
    }
}
