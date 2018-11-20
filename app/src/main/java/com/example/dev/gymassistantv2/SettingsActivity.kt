package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button

class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonChooseExercises = findViewById<Button>(R.id.buttonChooseAvailableExercises)
        val intentChooseExercises = Intent(this, ChooseAvailableExercisesActivity::class.java)
        buttonChooseExercises.setOnClickListener { startActivity(intentChooseExercises) }

        val buttonAddExercise = findViewById<Button>(R.id.buttonAddExercise)
        val intentAddExercise = Intent(this, AddExerciseActivity::class.java)
        buttonAddExercise.setOnClickListener { startActivity(intentAddExercise) }
    }
}