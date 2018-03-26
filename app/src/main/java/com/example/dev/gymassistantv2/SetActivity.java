package com.example.dev.gymassistantv2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SetActivity extends Activity {

    final int MSG_START_TIMER = 0;
    final int MSG_STOP_TIMER = 1;
    final int MSG_UPDATE_TIMER = 2;
    final int MSG_RESET_TIMER = 3;

    Stopwatch timer = new Stopwatch();
    final int REFRESH_RATE = 1;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TextView tvTimer = findViewById(R.id.textViewTimer);
            switch (msg.what) {

                case MSG_START_TIMER:
                    timer.start();
                    mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
                    break;

                case MSG_UPDATE_TIMER:
                    tvTimer.setText(timer.getFormattedElapsedTime());
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER,REFRESH_RATE);
                    break;

                case MSG_STOP_TIMER:
                    mHandler.removeMessages(MSG_UPDATE_TIMER);
                    timer.stop();
                    tvTimer.setText(timer.getFormattedElapsedTime());
                    break;

                case MSG_RESET_TIMER:
                    mHandler.removeMessages(MSG_UPDATE_TIMER);
                    timer.reset();
                    tvTimer.setText(timer.getFormattedElapsedTime());

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Call base class constructor
        super.onCreate(savedInstanceState);

        //ExerciseSet content view
        setContentView(R.layout.activity_set);

        //ExerciseSet timer
        final Intent stopwatchIntent = getIntent();
        timer.set(stopwatchIntent.getLongExtra("stopwatchElapsedTime", 0));
        boolean isStopwatchRunning = stopwatchIntent.getBooleanExtra("stopwatchRunning", false);
        if(isStopwatchRunning) {
            mHandler.sendEmptyMessage(MSG_START_TIMER);
        }

        //ExerciseSet style for all views
        TextView textView = findViewById(R.id.textViewTimer);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/BlackOpsOne-Regular.ttf");
        textView.setTypeface(typeface);
        textView = findViewById(R.id.textViewRepCount);
        textView.setTypeface(typeface);
        textView = findViewById(R.id.textViewWeight);
        textView.setTypeface(typeface);
        textView = findViewById(R.id.textViewExerciseName);
        textView.setTypeface(typeface);
        textView = findViewById(R.id.textViewSeries);
        textView.setTypeface(typeface);
        textView = findViewById(R.id.textViewSeriesNumber);
        textView.setTypeface(typeface);

        Button button = findViewById(R.id.buttonStart);
        button.setTypeface(typeface);
        button = findViewById(R.id.buttonStop);
        button.setTypeface(typeface);
        button = findViewById(R.id.buttonReset);
        button.setTypeface(typeface);
        button = findViewById(R.id.buttonNextExcerciseOK);
        button.setText(Html.fromHtml("Następne ćwiczenie<br><small>Zapisz serię</small>"));
        button.setTypeface(typeface);
        button = findViewById(R.id.buttonNextExerciseCancel);
        button.setText(Html.fromHtml("Następne ćwiczenie<br><small>Anuluj serię</small>"));
        button.setTypeface(typeface);
        button = findViewById(R.id.buttonNextSeries);
        button.setTypeface(typeface);

        EditText editText = findViewById(R.id.editTextRepCount);
        editText.setTypeface(typeface);
        editText = findViewById(R.id.editTextWeight);
        editText.setTypeface(typeface);

        //Read intent and set corresponding elements
        final Intent intent = getIntent();
        final String exerciseName = intent.getStringExtra("exerciseName");
        textView = findViewById(R.id.textViewExerciseName);
        textView.setText(exerciseName);
        if(exerciseName.length() >= 35) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }
        textView = findViewById(R.id.textViewSeriesNumber);
        textView.setText(Integer.toString(intent.getIntExtra("seriesNumber", 1)));

        //ExerciseSet stopwatch start button on click listeners
        button = findViewById(R.id.buttonStart);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mHandler.sendEmptyMessage(MSG_START_TIMER);
            }
        });

        //ExerciseSet stopwatch stop button on click listener
        button = findViewById(R.id.buttonStop);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mHandler.sendEmptyMessage(MSG_STOP_TIMER);
            }
        });

        //ExerciseSet stopwatch reset button on click listener
        button = findViewById(R.id.buttonReset);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mHandler.sendEmptyMessage(MSG_RESET_TIMER);
            }
        });

        //ExerciseSet next series button on click listener
        button = findViewById(R.id.buttonNextSeries);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                //Check if both text fields are filled
                EditText etWeight = findViewById(R.id.editTextWeight);
                String test = etWeight.getText().toString();
                EditText etReps = findViewById(R.id.editTextRepCount);
                if(!etWeight.getText().toString().equals("") && !etReps.getText().toString().equals("")) {

                    //Save set to database
                    long segmentID = intent.getLongExtra("segmentID", 0);
                    int repCount = Integer.parseInt(etReps.getText().toString());
                    int weight = Integer.parseInt(etWeight.getText().toString());
                    saveSetToDatabase(segmentID, repCount, weight);

                    //Prepare intent and redirect to choose exercise activity
                    int seriesNumber = intent.getIntExtra("seriesNumber", 1) + 1;
                    Intent newIntent = new Intent(getApplicationContext(), SetActivity.class);
                    newIntent.putExtra("exerciseName", exerciseName);
                    newIntent.putExtra("seriesNumber", seriesNumber);
                    newIntent.putExtra("stopwatchRunning", timer.isRunning());
                    newIntent.putExtra("stopwatchElapsedTime", timer.getElapsedTime());
                    newIntent.putExtra("workoutID", getIntent().getLongExtra("workoutID", 0));
                    newIntent.putExtra("segmentID", segmentID);
                    finish();
                    startActivity(newIntent);
                }

                //If at least one field is not filled, display message
                else {
                    Toast toast = Toast.makeText(
                            getApplicationContext(),
                            "Uzupełnij oba pola lub przejdź do kolejnego ćwiczenia",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        //ExerciseSet next exercise (cancel set) button on click listener
        button = findViewById(R.id.buttonNextExerciseCancel);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                //If there are no exerciseSets in segment, delete the segment
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                long segmentID = intent.getLongExtra("segmentID", 0);
                List<ExerciseSet> exerciseSets = db.getSetsBySegmentID(segmentID);
                if(exerciseSets.size() == 0) {
                    db.deleteSegment(segmentID);
                }

                //Prepare intent and refirect to choose exercise activity
                Intent intent = new Intent(getApplicationContext(), ChooseExcerciseActivity.class);
                intent.putExtra("workoutID", getIntent().getLongExtra("workoutID", 0));
                finish();
                startActivity(intent);
            }
        });

        //ExerciseSet next exercise (save set) button on click listener
        button = findViewById(R.id.buttonNextExcerciseOK);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                //Check if both text fields are filled
                EditText etWeight = findViewById(R.id.editTextWeight);
                String test = etWeight.getText().toString();
                EditText etReps = findViewById(R.id.editTextRepCount);
                if (!etWeight.getText().toString().equals("") && !etReps.getText().toString().equals("")) {

                    //Save set to database
                    long segmentID = intent.getLongExtra("segmentID", 0);
                    int repCount = Integer.parseInt(etReps.getText().toString());
                    int weight = Integer.parseInt(etWeight.getText().toString());
                    saveSetToDatabase(segmentID, repCount, weight);

                    //Prepare intent and redirect to choose exercise activity
                    Intent intent = new Intent(getApplicationContext(), ChooseExcerciseActivity.class);
                    intent.putExtra("workoutID", getIntent().getLongExtra("workoutID", 0));
                    finish();
                    startActivity(intent);
                }

                //If at least one field is empty, display warning
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Uzupełnij oba pola bądź anuluj serię",
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    /**
     * Create set and save it in database
     * @param segmentID ID of the segment to which set should be assigned
     * @param repCount Number of reps in series
     * @param weight Series weight
     * @return ID of the set in database
     */
    private long saveSetToDatabase(long segmentID, int repCount, int weight) {

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        ExerciseSet exerciseSet = new ExerciseSet(segmentID, repCount, weight);
        return db.createSet(exerciseSet);
    }

    /**
     * Disable back button
     */
    @Override
    public void onBackPressed() {
    }
}
