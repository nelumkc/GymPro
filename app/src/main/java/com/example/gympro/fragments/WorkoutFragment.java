package com.example.gympro.fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gympro.R;
import com.example.gympro.adapters.HintAdapter;
import com.example.gympro.adapters.ExerciseAdapter;
import com.example.gympro.Model.Exercise;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WorkoutFragment extends Fragment {

    private Spinner spinnerExerciseType;
    private Spinner spinnerMuscleGroup;
    private Spinner spinnerDifficultyLevel;
    private Button buttonFilter;
    private RecyclerView recyclerViewExercises;
    private ExerciseAdapter exerciseAdapter;

    RecyclerView.LayoutManager layoutManager;
   // private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container, false);

        spinnerExerciseType = view.findViewById(R.id.spinner_exercise_type);
        spinnerMuscleGroup = view.findViewById(R.id.spinner_muscle_group);
        spinnerDifficultyLevel = view.findViewById(R.id.spinner_difficulty_level);
        buttonFilter = view.findViewById(R.id.button_filter);
        recyclerViewExercises = view.findViewById(R.id.recycler_view_exercises);

        // Set up RecyclerView
        recyclerViewExercises.setLayoutManager(new LinearLayoutManager(getContext()));
        exerciseAdapter = new ExerciseAdapter(new ArrayList<>(), getContext());
        recyclerViewExercises.setAdapter(exerciseAdapter);

        // Populate spinners
        populateSpinners();

        // Set filter button click listener
        buttonFilter.setOnClickListener(v -> filterExercises());

        if (savedInstanceState != null) {
            String selectedExerciseType = savedInstanceState.getString("selectedExerciseType", "");
            String selectedMuscleGroup = savedInstanceState.getString("selectedMuscleGroup", "");
            String selectedDifficultyLevel = savedInstanceState.getString("selectedDifficultyLevel", "");

            // Set the saved filter values to the spinners
            setSpinnerSelection(spinnerExerciseType, selectedExerciseType);
            setSpinnerSelection(spinnerMuscleGroup, selectedMuscleGroup);
            setSpinnerSelection(spinnerDifficultyLevel, selectedDifficultyLevel);

            // Fetch exercises based on the saved filter values
            fetchExercises(selectedExerciseType, selectedMuscleGroup, selectedDifficultyLevel);
        } else {
            // Load all exercises if no filters are saved
            loadAllExercises();
        }


        return view;
    }

    private void loadAllExercises() {
        fetchExercises("", "", "");
    }

    private void populateSpinners() {
        HintAdapter exerciseTypeAdapter = new HintAdapter(getContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.exercise_types));
        exerciseTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExerciseType.setAdapter(exerciseTypeAdapter);
        spinnerExerciseType.setSelection(0);

        HintAdapter muscleGroupAdapter = new HintAdapter(getContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.muscle_groups));
        muscleGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMuscleGroup.setAdapter(muscleGroupAdapter);
        spinnerMuscleGroup.setSelection(0);

        HintAdapter difficultyLevelAdapter = new HintAdapter(getContext(),
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.difficulty_levels));
        difficultyLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficultyLevel.setAdapter(difficultyLevelAdapter);
        spinnerDifficultyLevel.setSelection(0);
    }

    private void filterExercises() {
        Toast.makeText(getContext(), "Button Pressed", Toast.LENGTH_SHORT).show();
        // Implement the logic to filter exercises based on selected criteria
        String selectedExerciseType = spinnerExerciseType.getSelectedItem().toString();
        String selectedMuscleGroup = spinnerMuscleGroup.getSelectedItem().toString();
        String selectedDifficultyLevel = spinnerDifficultyLevel.getSelectedItem().toString();

        // Fetch and display filtered exercises from API
        fetchExercises(selectedExerciseType, selectedMuscleGroup, selectedDifficultyLevel);
    }

    private void fetchExercises(String exerciseType, String muscleGroup, String difficultyLevel) {
        new Thread(() -> {
            List<Exercise> filteredExercises = new ArrayList<>();
            HttpURLConnection connection = null;
            InputStream responseStream = null;
            try {
                String apiurl = "https://api.api-ninjas.com/v1/exercises";
                String apiKey = "3VKP8UBo2TwVtVTL8gQ5aQ==fmFIV4OzOXG18BYO";
                //String apiUrlWithParams = String.format("%s?type=%s&muscle=%s&difficulty=%s", apiurl, exerciseType, muscleGroup, difficultyLevel);
                // Build query parameters conditionally
                StringBuilder apiUrlWithParams = new StringBuilder(apiurl);
                apiUrlWithParams.append("?");

                if (!exerciseType.isEmpty() && !exerciseType.equals("Exercise Type")) {
                    apiUrlWithParams.append("type=").append(exerciseType).append("&");
                }
                if (!muscleGroup.isEmpty() && !muscleGroup.equals("Muscle Group")) {
                    apiUrlWithParams.append("muscle=").append(muscleGroup).append("&");
                }
                if (!difficultyLevel.isEmpty() && !difficultyLevel.equals("Difficulty Level")) {
                    apiUrlWithParams.append("difficulty=").append(difficultyLevel).append("&");
                }

                // Remove the last '&' or '?' if present
                if (apiUrlWithParams.charAt(apiUrlWithParams.length() - 1) == '&' ||
                        apiUrlWithParams.charAt(apiUrlWithParams.length() - 1) == '?') {
                    apiUrlWithParams.setLength(apiUrlWithParams.length() - 1);
                }

                // Show the API URL
                Log.d(TAG, "API URL: " + apiUrlWithParams);
                URL url = new URL(apiUrlWithParams.toString());

                // Open a connection
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("accept", "application/json");
                connection.setRequestProperty("X-Api-Key", apiKey);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    responseStream = connection.getInputStream();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(responseStream);

                    filteredExercises = parseExercisesFromJson(root);
                } else {
                    System.out.println("Error: " + responseCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (responseStream != null) {
                        responseStream.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Load image data from JSON file
            Map<String, String> imageMap = loadImageData();

            // Match exercises with images
            for (Exercise exercise : filteredExercises) {
                String imageUrl = imageMap.get(exercise.getHashcode());
                if (imageUrl != null) {
                    exercise.setImageUrl(imageUrl);
                }
            }

            // Update the RecyclerView on the UI thread
            List<Exercise> finalFilteredExercises = filteredExercises;
            getActivity().runOnUiThread(() -> exerciseAdapter.setExercises(finalFilteredExercises));
        }).start();
    }

    private List<Exercise> parseExercisesFromJson(JsonNode root) {
        List<Exercise> exercises = new ArrayList<>();
        if (root.isArray()) {
            for (JsonNode node : root) {
                Exercise exercise = new Exercise();
                exercise.setName(node.path("name").asText());
                exercise.setMuscle(node.path("muscle").asText());
                exercise.setEquipment(node.path("equipment").asText());
                exercise.setDifficulty(node.path("difficulty").asText());
                exercise.setInstructions(node.path("instructions").asText());
                exercise.setHashcode("ABC".hashCode()+"");
                //exercise.setImageUrl(node.path("image_url").asText());
                exercises.add(exercise);
            }
        }
        return exercises;
    }

    private Map<String, String> loadImageData() {
        Map<String, String> imageMap = new HashMap<>();
        try {
            InputStream inputStream = getContext().getAssets().open("exerciseImages.json");
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(inputStream);
            for (JsonNode node : root.path("exercises")) {
                String hashCode = node.path("hash_code").asText();
                String imageUrl = node.path("image_url").asText();
                imageMap.put(hashCode, imageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageMap;
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        if (adapter != null) {
            int position = adapter.getPosition(value);
            if (position >= 0) {
                spinner.setSelection(position);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("selectedExerciseType", spinnerExerciseType.getSelectedItem().toString());
        outState.putString("selectedMuscleGroup", spinnerMuscleGroup.getSelectedItem().toString());
        outState.putString("selectedDifficultyLevel", spinnerDifficultyLevel.getSelectedItem().toString());
    }
}
