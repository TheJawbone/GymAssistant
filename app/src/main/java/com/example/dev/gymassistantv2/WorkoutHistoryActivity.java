package com.example.dev.gymassistantv2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorkoutHistoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Call base class constructor
        super.onCreate(savedInstanceState);

        //Create scroll view
        ScrollView scrollView = new ScrollView(getApplicationContext());

        //Calculate logical density
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;

        //Create layout and add it to scroll view
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding((int)Math.ceil(10 * logicalDensity), 0, (int)Math.ceil(10 * logicalDensity), 0);
        scrollView.addView(layout);

        //Get all workouts from database
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<CompletedWorkout> workouts = db.getAllCompletedWorkouts();

        //Create buttons for each completed workout
        for(final CompletedWorkout workout : workouts) {

            String[] dateStringArray = workout.getCreatedAt().split(" ");
            String date = dateStringArray[0];
            List<Segment>segments = db.getSegmentsByWorkoutID(workout.getID());
            Set<String> muscleGroupNames = new HashSet<>();
            for(Segment segment : segments) {
                Exercise exercise = db.getExerciseByID(segment.getExerciseID());
                List<MuscleGroup> muscleGroups = db.getMuscleGroupsByExerciseID(exercise.getID());
                for(MuscleGroup muscleGroup : muscleGroups) {
                    muscleGroupNames.add(muscleGroup.getName());
                }
            }
            String buttonText = date + "\n";
            for(String muscleGroupString : muscleGroupNames) {
                buttonText += muscleGroupString + " ";
            }

            Button button = new Button(getApplicationContext());
            button.setHeight((int) Math.ceil(60 * logicalDensity));
            button.setBackground(getApplicationContext().getDrawable(R.drawable.bottom_border));
            button.setText(buttonText);
            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BlackOpsOne-Regular.ttf");
            button.setTypeface(typeface);

            button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), WorkoutDetailsActivity.class);
                    intent.putExtra("workoutID", workout.getID());
                    startActivity(intent);
                }
            });

            //Add the button to layout
            layout.addView(button);
        }

        //ExerciseSet content view
        setContentView(scrollView);
    }
}
