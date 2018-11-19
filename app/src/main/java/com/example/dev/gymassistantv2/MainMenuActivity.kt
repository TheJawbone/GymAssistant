package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainMenuActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val buttonBeginWorkout = findViewById<Button>(R.id.buttonBeginWorkout)
        val intentBeginWorkout = Intent(this, ChooseExerciseActivity::class.java)
        buttonBeginWorkout.setOnClickListener { startActivity(intentBeginWorkout) }

        val buttonProgress = findViewById<Button>(R.id.buttonProgress)
        val intentProgress = Intent(this, ProgressActivity::class.java)
        buttonProgress.setOnClickListener { startActivity(intentProgress) }

        val buttonHistory = findViewById<Button>(R.id.buttonHistory)
        val intentHistory = Intent(this, WorkoutHistoryActivity::class.java)
        buttonHistory.setOnClickListener { startActivity(intentHistory) }

        val buttonSettings = findViewById<Button>(R.id.buttonSettings)
        val intentSettings = Intent(this, SettingsActivity::class.java)
        buttonSettings.setOnClickListener { startActivity(intentSettings) }
    }
}
