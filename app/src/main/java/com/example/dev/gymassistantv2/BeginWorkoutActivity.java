package com.example.dev.gymassistantv2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BeginWorkoutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Call super class constructor
        super.onCreate(savedInstanceState);

        //Set content view
        setContentView(R.layout.activity_begin_workout);

        //Set font for title and buttons
        TextView textView = findViewById(R.id.textViewTitle);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BlackOpsOne-Regular.ttf");
        textView.setTypeface(typeface);

        Button button = findViewById(R.id.buttonFreeWorkout);
        button.setTypeface(typeface);
        button = findViewById(R.id.buttonPlannedWorkout);
        button.setTypeface(typeface);
        button = findViewById(R.id.buttonBack);
        button.setTypeface(typeface);

        //Create free workout on click listener
        button = findViewById(R.id.buttonFreeWorkout);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //Create workout and save it in database
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                CompletedWorkout workout = new CompletedWorkout();
                long workoutID = db.createCompletedWorkout(workout);

                //Redirect to choose exercise activity
                Intent intent = new Intent(getApplicationContext(), ChooseExcerciseActivity.class);
                intent.putExtra("workoutID", workoutID);
                finish();
                startActivity(intent);
            }
        });

        //Set back button on click listener
        button = findViewById(R.id.buttonBack);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

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
