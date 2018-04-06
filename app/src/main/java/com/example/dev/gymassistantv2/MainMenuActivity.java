package com.example.dev.gymassistantv2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.OutputStreamWriter;
import java.util.List;

public class MainMenuActivity extends Activity {

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Call super class constructor
        super.onCreate(savedInstanceState);

        //ExerciseSet content view
        setContentView(R.layout.activity_main_menu);

        //ExerciseSet title's font
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        Typeface typeface = Typeface.createFromAsset(getAssets(),  "fonts/BlackOpsOne-Regular.ttf");
        textViewTitle.setTypeface(typeface);

        //ExerciseSet buttons' font
        Button button = findViewById(R.id.buttonBeginWorkout);
        button.setTypeface(typeface);
        button = findViewById(R.id.buttonPlanWorkout);
        button.setTypeface(typeface);
        button = findViewById(R.id.buttonProgress);
        button.setTypeface(typeface);
        button = findViewById(R.id.buttonHistory);
        button.setTypeface(typeface);
        button = findViewById(R.id.buttonSettings);
        button.setTypeface(typeface);

        //Create begin workout on click listener
        button = findViewById(R.id.buttonBeginWorkout);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BeginWorkoutActivity.class);
                finish();
                startActivity(intent);
            }
        });

        //Create workout history on click listener
        button = findViewById(R.id.buttonHistory);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WorkoutHistoryActivity.class);
                startActivity(intent);
            }
        });

        //Create settings workout on click listener
        button = findViewById(R.id.buttonSettings);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        //Create/open database
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        //List all muscle groups
        List<MuscleGroup> muscleGroups = db.getAllMuscleGroups();
        for(MuscleGroup muscleGroup : muscleGroups) {
            System.out.println(muscleGroup.getName());
        }
    }
}
