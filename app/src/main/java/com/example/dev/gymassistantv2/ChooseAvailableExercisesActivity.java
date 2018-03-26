package com.example.dev.gymassistantv2;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class ChooseAvailableExercisesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Call base class constructor
        super.onCreate(savedInstanceState);

        //ExerciseSet content view
        setContentView(R.layout.activity_choose_available_exercises);

        //ExerciseSet fonts
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BlackOpsOne-Regular.ttf");
        TextView textView = findViewById(R.id.textViewMuscleGroups);
        textView.setTypeface(typeface);
        textView = findViewById(R.id.textViewSort);
        textView.setTypeface(typeface);

        //ExerciseSet up muscle group spinner
        final DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<MuscleGroup> muscleGroups = db.getAllMuscleGroups();
        String[] filterSpinnerArray = new String[muscleGroups.size()];
        int i = 0;
        for(MuscleGroup muscleGroup : muscleGroups) {
            filterSpinnerArray[i] = muscleGroup.getName();
            i++;
        }
        final Spinner spinnerFilter = findViewById(R.id.spinnerMuscleGroups);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, filterSpinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter);

        //TODO: ExerciseSet up sort spinner

        //List all exercises as checkboxes and plane them in the linear layout
        List<Exercise> exercises = db.getAllExercises();
        LinearLayout layout = findViewById(R.id.linearLayout);
        /*for(Exercise exercise : exercises) {
            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setText(exercise.getName());
            checkBox.setBackground(getApplicationContext().getDrawable(R.drawable.bottom_border));
            layout.addView(checkBox);
        }*/
    }
}
