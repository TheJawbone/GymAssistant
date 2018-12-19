package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.dev.gymassistantv2.database.GymAssistantDatabase
import com.example.dev.gymassistantv2.dtos.UserDto

class SettingsActivity : Activity() {

    private lateinit var loggedUser: UserDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        determineLoggedUserData()
        val buttonClearWorkoutHistory = findViewById<Button>(R.id.buttonClearWorkoutsInDatabase)
        buttonClearWorkoutHistory.setOnClickListener{
            val dbContext = GymAssistantDatabase.getInstance(this)
            dbContext!!.workoutDao().delete(dbContext.workoutDao().getForUser(loggedUser.userId!!))
            Toast.makeText(applicationContext, "Historia trenengów została wyczyszczona!",
                    Toast.LENGTH_LONG).show()
        }

        val buttonChooseExercises = findViewById<Button>(R.id.buttonChooseAvailableExercises)
        val intentChooseExercises = Intent(this, ChooseAvailableExercisesActivity::class.java)
        buttonChooseExercises.setOnClickListener { startActivity(intentChooseExercises) }

        val buttonAddExercise = findViewById<Button>(R.id.buttonAddExercise)
        val intentAddExercise = Intent(this, AddExerciseActivity::class.java)
        buttonAddExercise.setOnClickListener { startActivity(intentAddExercise) }
    }

    private fun determineLoggedUserData() {
        loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
    }

    override fun onBackPressed() {
        finish()
    }
}