package com.example.dev.gymassistantv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChooseExcerciseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Call super class' constructor
        super.onCreate(savedInstanceState);

        //ExerciseSet content view
        setContentView(R.layout.activity_choose_excercise);

        //ExerciseSet font for views
        TextView textView = findViewById(R.id.textViewTitle);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BlackOpsOne-Regular.ttf");
        textView.setTypeface(typeface);

        textView = findViewById(R.id.textViewFilter);
        textView.setTypeface(typeface);

        textView = findViewById(R.id.textViewExcerciseList);
        textView.setTypeface(typeface);

        Button button = findViewById(R.id.buttonBeginSet);
        button.setTypeface(typeface);
        button = findViewById(R.id.buttonEndWorkout);
        button.setTypeface(typeface);

        //Read workout ID from intent
        Intent intent = getIntent();
        final long workoutID = intent.getLongExtra("workoutID", 0);

        //Set up muscle group spinner
        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<MuscleGroup> muscleGroups = db.getAllMuscleGroups();
        String[] muscleGroupSpinnerArray = new String[muscleGroups.size()];
        int i = 0;
        for(MuscleGroup muscleGroup : muscleGroups) {
            muscleGroupSpinnerArray[i] = muscleGroup.getName();
            i++;
        }
        final Spinner spinnerMuscleGroups = findViewById(R.id.spinnerMuscleGroups);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, muscleGroupSpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMuscleGroups.setAdapter(adapter);

        //Set up exercise list spinner
        List<Exercise> exercises = db.getAllExercises();
        String[] exerciseListSpinnerArray = new String[exercises.size()];
        i = 0;
        for(Exercise exercise : exercises) {
            exerciseListSpinnerArray[i] = exercise.getName();
            i++;
        }
        final Spinner spinnerExercises = findViewById(R.id.spinnerExcerciseList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exerciseListSpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExercises.setAdapter(adapter);

        //Set muscle group spinner listener
        final Context context = getApplicationContext();
        spinnerMuscleGroups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedItem = spinnerMuscleGroups.getSelectedItem().toString();
                Set exerciseNames = new HashSet();
                if(selectedItem.equals("Dowolna")) {
                    List<Exercise> exercises = db.getAllExercises();
                    for(Exercise exercise : exercises) {
                        exerciseNames.add(exercise.getName());
                    }
                } else {
                    MuscleGroup muscleGroup = db.getMuscleGroupByID(selectedItem);
                    List<Exercise> exercises = db.getExercisesByMuscleGroupID(muscleGroup.getID());
                    for(Exercise exercise : exercises) {
                        exerciseNames.add(exercise.getName());
                    }
                }
                String[] exerciseListSpinnerArray = new String[exerciseNames.size()];
                i = 0;
                for(Object exerciseName : exerciseNames) {
                    exerciseListSpinnerArray[i] = (String)exerciseName;
                    i++;
                }
                final Spinner spinnerExercises = findViewById(R.id.spinnerExcerciseList);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context.getApplicationContext(), android.R.layout.simple_spinner_item, exerciseListSpinnerArray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerExercises.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //ExerciseSet start series button on click listener
        button = findViewById(R.id.buttonBeginSet);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                //Create segment in database
                String exerciseName = spinnerExercises.getSelectedItem().toString();
                long exerciseID = db.getExerciseByName(exerciseName).getID();
                Segment segment = new Segment(exerciseID, workoutID);
                long segmentID = db.createSegment(segment);

               Intent intent = new Intent(getApplicationContext(), SetActivity.class);
               intent.putExtra("exerciseName", exerciseName);
               intent.putExtra("seriesNumber", 1);
               intent.putExtra("stopwatchRunning", false);
               intent.putExtra("stopwatchElapsedTime", 0);
               intent.putExtra("segmentID", segmentID);
               intent.putExtra("workoutID", workoutID);
               finish();
               startActivity(intent);
           }
        });

        //ExerciseSet end workout button on click listener
        button = findViewById(R.id.buttonEndWorkout);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                //If there are no segments in the workout, delete it from the database
                List<Segment> segments = db.getSegmentsByWorkoutID(workoutID);
                if(segments.size() == 0) {
                    db.deleteCompletedWorkout(workoutID);
                }

                //Redirect to main menu activity
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    /**
     * Disable back button
     */
    @Override
    public void onBackPressed() {
    }
}
