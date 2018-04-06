package com.example.dev.gymassistantv2;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("ALL")
public class ChooseAvailableExercisesActivity extends Activity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

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

        //Set up muscle group spinner
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<MuscleGroup> muscleGroups = db.getAllMuscleGroups();
        String[] spinnerArray = new String[muscleGroups.size()];
        int i = 0;
        for(MuscleGroup muscleGroup : muscleGroups) {
            spinnerArray[i] = muscleGroup.getName();
            i++;
        }
        final Spinner spinnerMuscleGroup = findViewById(R.id.spinnerMuscleGroups);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMuscleGroup.setAdapter(adapter);
        spinnerMuscleGroup.setOnItemSelectedListener(this);

        //Set up sort spinner
        spinnerArray = new String[] {
                getString(R.string.a_z),
                getString(R.string.z_a)
        };
        final Spinner spinnerSort = findViewById(R.id.spinnerSort);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);
        spinnerSort.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Spinner spinner = findViewById(R.id.spinnerMuscleGroups);
        String muscleGroupName = spinner.getSelectedItem().toString();
        spinner = findViewById(R.id.spinnerSort);
        String sortValue = spinner.getSelectedItem().toString();

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        MuscleGroup muscleGroup = db.getMuscleGroupByName(muscleGroupName);
        List<Exercise> exerciseList = db.getExercisesByMuscleGroupID(muscleGroup.getID());
        if(sortValue.equals("Z-A")) {
            Collections.reverse(exerciseList);
        }
        LinearLayout layout = findViewById(R.id.linearLayout);
        layout.removeAllViews();
        for(Exercise exercise : exerciseList) {
            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setText(exercise.getName());
            checkBox.setBackground(getApplicationContext().getDrawable(R.drawable.bottom_border));
            checkBox.setChecked(exercise.getIsAvailable());
            checkBox.setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            checkBox.setId((int)exercise.getID());
            checkBox.setOnCheckedChangeListener(this);
            layout.addView(checkBox);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onCheckedChanged(CompoundButton cButton, boolean checked) {

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        Exercise exercise = db.getExerciseByID(cButton.getId());
        exercise.setIsAvailable(cButton.isChecked());
        db.updateExercise(exercise);
    }
}
