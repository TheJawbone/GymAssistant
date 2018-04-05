package com.example.dev.gymassistantv2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Call super class constructor
        super.onCreate(savedInstanceState);

        //ExerciseSet content view
        setContentView(R.layout.activity_settings);

        //ExerciseSet fonts
        Typeface typeface = Typeface.createFromAsset(getAssets(),  "fonts/BlackOpsOne-Regular.ttf");

        Button button = findViewById(R.id.buttonClearDatabase);
        button.setTypeface(typeface);
        button = findViewById(R.id.buttonChooseAvailableExercises);
        button.setTypeface(typeface);

        //ExerciseSet clear database button on click listener
        button = findViewById(R.id.buttonClearDatabase);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                db.deleteAllCompletedWorkouts();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "@strings/history_erased_notification",
                        Toast.LENGTH_LONG);
                toast.show();
            }
        });

        //ExerciseSet choose available exercises button on click listener
        button = findViewById(R.id.buttonChooseAvailableExercises);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChooseAvailableExercisesActivity.class);
                startActivity(intent);
            }
        });
    }
}
