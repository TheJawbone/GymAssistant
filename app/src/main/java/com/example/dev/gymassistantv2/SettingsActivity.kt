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
        setClearHistoryButton()
        setChooseExercisesButton()
        setResetToDefaultButton()
        setAddExerciseButton()
    }

    private fun determineLoggedUserData() {
        loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
    }

    private fun setClearHistoryButton() {
        val buttonClearWorkoutHistory = findViewById<Button>(R.id.buttonClearWorkoutsInDatabase)
        buttonClearWorkoutHistory.setOnClickListener {
            clearWorkoutHistory()
            Toast.makeText(applicationContext, "Historia trenengów została wyczyszczona!",
                    Toast.LENGTH_LONG).show()
        }
    }

    private fun clearWorkoutHistory() {
        val dbContext = GymAssistantDatabase.getInstance(this)
        dbContext!!.workoutDao().delete(dbContext.workoutDao().getForUser(loggedUser.userId!!))
    }

    private fun setChooseExercisesButton() {
        val buttonChooseExercises = findViewById<Button>(R.id.buttonChooseAvailableExercises)
        val intentChooseExercises = Intent(this, ChooseAvailableExercisesActivity::class.java)
        buttonChooseExercises.setOnClickListener { startActivity(intentChooseExercises) }
    }

    private fun setResetToDefaultButton() {
        findViewById<Button>(R.id.buttonDefaultSettings).setOnClickListener {
            clearWorkoutHistory()
            deleteCustomExercises()
            deleteTrainerAndCharges()
            Toast.makeText(applicationContext, "Aplikacja zostałą zresetowana do ustawień domyślnych!",
                    Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteCustomExercises() {
        val dbContext = GymAssistantDatabase.getInstance(this)
        dbContext!!.exerciseDao().delete(loggedUser.userId!!)
    }

    private fun deleteTrainerAndCharges() {
        val dbContext = GymAssistantDatabase.getInstance(this)
        if (loggedUser.isTrainer!!) {
            var charges = dbContext!!.userDao().getChargesForUser(loggedUser.userId!!)
            charges.forEach {
                it.trainerId = null
                dbContext!!.userDao().update(it)
            }
        } else {
            loggedUser.trainerId = null
            dbContext!!.userDao().update(loggedUser.toUser())
        }
    }

    private fun setAddExerciseButton() {
        val buttonAddExercise = findViewById<Button>(R.id.buttonAddExercise)
        val intentAddExercise = Intent(this, AddExerciseActivity::class.java)
        buttonAddExercise.setOnClickListener {
            intentAddExercise.putExtra("loggedUser", loggedUser)
            startActivity(intentAddExercise)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}