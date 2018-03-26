package com.example.dev.gymassistantv2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class WorkoutDetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Call base class constructor
        super.onCreate(savedInstanceState);

        //ExerciseSet content view
        setContentView(R.layout.activity_workout_details);

        //ExerciseSet fonts
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BlackOpsOne-Regular.ttf");
        TextView textView = findViewById(R.id.textViewWorkoutDetails);
        textView.setTypeface(typeface);
        textView.setText("");

        //Read and display workout details
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        Intent intent = getIntent();
        CompletedWorkout workout = db.getCompletedWorkout(intent.getLongExtra("workoutID", 0));
        List<Segment> segments = db.getSegmentsByWorkoutID(workout.getID());
        for(Segment segment : segments) {
            List<ExerciseSet> exerciseSets = db.getSetsBySegmentID(segment.getID());
            Exercise exercise = db.getExerciseByID(segment.getExerciseID());
            textView.setText(textView.getText() + exercise.getName() + "\n");
            int setCounter = 1;
            for(ExerciseSet exerciseSet : exerciseSets) {
                textView.setText(textView.getText() + "   Seria " + setCounter
                        + "\n   Weight: " + exerciseSet.getWeight() + "\n   Reps: " + exerciseSet.getRepCount() + "\n");
                setCounter++;
            }
            textView.setText(textView.getText() + "\n");
        }
    }
}
