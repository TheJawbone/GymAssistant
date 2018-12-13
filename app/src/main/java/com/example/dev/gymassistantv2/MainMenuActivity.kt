package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import com.example.dev.gymassistantv2.Database.DBInitializer
import com.example.dev.gymassistantv2.Database.GymAssistantDatabase

class MainMenuActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        setNavigationListeners()
        setDatabase()
    }

    private fun setNavigationListeners() {
        val buttonBeginWorkout = findViewById<Button>(R.id.buttonBeginWorkout)
        val intentBeginWorkout = Intent(this, ChooseExerciseActivity::class.java)
        buttonBeginWorkout.setOnClickListener { startActivity(intentBeginWorkout) }

        val buttonProgress = findViewById<Button>(R.id.buttonProgress)
        val intentProgress = Intent(this, ProgressSubmenuActivity::class.java)
        buttonProgress.setOnClickListener { startActivity(intentProgress) }

        val isTrainer = false // TODO: check if the user is a trainer
        val buttonTrainerOrCharges = findViewById<Button>(R.id.buttonTrainerOrCharges)
        if (!isTrainer) {
            buttonTrainerOrCharges.text = resources.getString(R.string.trainer)
            val intentTrainer = Intent(this, ManageTrainerActivity::class.java)
            buttonTrainerOrCharges.setOnClickListener { startActivity(intentTrainer) }
        } else {
            buttonTrainerOrCharges.text = resources.getString(R.string.your_charges)
            val intentCharges = Intent(this, ChargesSubmenuActivity::class.java)
            buttonTrainerOrCharges.setOnClickListener { startActivity(intentCharges) }
        }

        val buttonHistory = findViewById<Button>(R.id.buttonHistory)
        val intentHistory = Intent(this, WorkoutHistoryActivity::class.java)
        buttonHistory.setOnClickListener { startActivity(intentHistory) }

        val buttonSettings = findViewById<Button>(R.id.buttonSettings)
        val intentSettings = Intent(this, SettingsActivity::class.java)
        buttonSettings.setOnClickListener { startActivity(intentSettings) }
    }

    private fun setDatabase() {
        //applicationContext.deleteDatabase("gymAssistantDb")
        val dbContext = GymAssistantDatabase.getInstance(applicationContext)
        val dbInitializer = DBInitializer(dbContext)
        dbInitializer.populateMuscleGroup()
        dbInitializer.populateExercise()
    }
}
