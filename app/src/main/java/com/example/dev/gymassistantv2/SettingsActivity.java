package com.example.dev.gymassistantv2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingsActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Call super class constructor
        super.onCreate(savedInstanceState);

        //ExerciseSet content view
        setContentView(R.layout.activity_settings);

        //ExerciseSet fonts
        Typeface typeface = Typeface.createFromAsset(getAssets(),  "fonts/BlackOpsOne-Regular.ttf");

        // Set up buttons
        Button button = findViewById(R.id.buttonClearDatabase);
        button.setTypeface(typeface);
        button.setOnClickListener(this);
        button = findViewById(R.id.buttonChooseAvailableExercises);
        button.setTypeface(typeface);
        button.setOnClickListener(this);
        button = findViewById(R.id.buttonDefaultSettings);
        button.setTypeface(typeface);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {

            case R.id.buttonClearDatabase:
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                db.truncateCompletedWorkouts();
                Toast toast = Toast.makeText(getApplicationContext(),
                        getString(R.string.history_erased_notification),
                        Toast.LENGTH_LONG);
                toast.show();
                break;

            case R.id.buttonChooseAvailableExercises:
                Intent intent = new Intent(getApplicationContext(), ChooseAvailableExercisesActivity.class);
                startActivity(intent);
                break;

            case R.id.buttonDefaultSettings:
                getApplicationContext().deleteDatabase(getString(R.string.database_name));
                db = new DatabaseHelper(getApplicationContext());
                db.populateExercisesTable(getApplicationContext());
                db.populateMuscleGroupsTable(getApplicationContext());
                toast = Toast.makeText(getApplicationContext(),
                        getString(R.string.default_settings_restored_notification),
                        Toast.LENGTH_LONG);
                toast.show();
                break;
        }
    }
}
