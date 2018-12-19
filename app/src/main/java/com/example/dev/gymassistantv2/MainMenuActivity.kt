package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.dev.gymassistantv2.dtos.PendingInvitationDto
import com.example.dev.gymassistantv2.dtos.UserDto
import com.example.dev.gymassistantv2.database.DBInitializer
import com.example.dev.gymassistantv2.database.GymAssistantDatabase
import com.example.dev.gymassistantv2.entities.Workout
import com.facebook.login.LoginManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainMenuActivity : Activity() {

    private lateinit var loggedUser: UserDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        determineLoggedUserData()
        setNavigationControls()
        setLogoutButton()
        setDatabase()

        Toast.makeText(applicationContext, "Logged as ${loggedUser.firstName} ${loggedUser.lastName}",
                Toast.LENGTH_LONG).show()
    }

    private fun determineLoggedUserData() {
        loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
    }

    private fun setNavigationControls() {
        val buttonBeginWorkout = findViewById<Button>(R.id.buttonBeginWorkout)
        val intentBeginWorkout = Intent(this, ChooseExerciseActivity::class.java)
        buttonBeginWorkout.setOnClickListener {
            val workout = Workout()
            workout.date = System.currentTimeMillis()
            workout.userId = loggedUser.userId
            val workoutId = GymAssistantDatabase.getInstance(this)!!.workoutDao().insert(workout)
            intentBeginWorkout.putExtra("workoutId", workoutId)
            intentBeginWorkout.putExtra("loggedUser", loggedUser)
            startActivity(intentBeginWorkout)
        }

        val buttonProgress = findViewById<Button>(R.id.buttonProgress)
        val intentProgress = Intent(this, ProgressSubmenuActivity::class.java)
        buttonProgress.setOnClickListener {
            intentProgress.putExtra("loggedUser", loggedUser)
            intentProgress.putExtra("historyOwnerId", loggedUser.userId)
            startActivity(intentProgress)
        }

        val buttonTrainerOrCharges = findViewById<Button>(R.id.buttonTrainerOrCharges)
        if (loggedUser.isTrainer!!) {
            buttonTrainerOrCharges.text = resources.getString(R.string.your_charges)
            val intentCharges = Intent(this, ChargesSubmenuActivity::class.java)
            intentCharges.putExtra("loggedUser", loggedUser)
            buttonTrainerOrCharges.setOnClickListener { startActivity(intentCharges) }
        } else {
            buttonTrainerOrCharges.text = resources.getString(R.string.trainer)
            if(loggedUser.trainerId != null) {
                val intentTrainer = Intent(this, FindTrainerActivity::class.java)
                intentTrainer.putExtra("loggedUser", loggedUser)
                intentTrainer.putExtra("pendingInvitation", PendingInvitationDto())
                buttonTrainerOrCharges.setOnClickListener { startActivity(intentTrainer) }
            } else {
                val intentTrainer = Intent(this, ManageTrainerActivity::class.java)
                intentTrainer.putExtra("loggedUser", loggedUser)
                buttonTrainerOrCharges.setOnClickListener { startActivity(intentTrainer) }
            }
        }

        val buttonHistory = findViewById<Button>(R.id.buttonHistory)
        val intentHistory = Intent(this, WorkoutHistoryActivity::class.java)
        intentHistory.putExtra("loggedUser", loggedUser)
        intentHistory.putExtra("historyOwnerId", loggedUser.userId)
        buttonHistory.setOnClickListener { startActivity(intentHistory) }

        val buttonSettings = findViewById<Button>(R.id.buttonSettings)
        val intentSettings = Intent(this, SettingsActivity::class.java)
        buttonSettings.setOnClickListener { startActivity(intentSettings) }
    }

    private fun setDatabase() {
        GlobalScope.launch {
            applicationContext.deleteDatabase("gymAssistantDb")
            val dbContext = GymAssistantDatabase.getInstance(applicationContext)
            val dbInitializer = DBInitializer(dbContext)
            dbInitializer.populateMuscleGroup()
            dbInitializer.populateExercise()
            dbInitializer.populateTrainers()
            if (loggedUser.isTrainer!!)
                dbInitializer.populateChargesForTrainer(loggedUser.userId!!)
            dbInitializer.generateWorkoutHistoryForUser(loggedUser.userId!!)
        }
    }

    private fun setLogoutButton() {
        val buttonLogout = findViewById<Button>(R.id.buttonLogout)
        val intentLogIn = Intent(this, LogInActivity::class.java)
        buttonLogout.setOnClickListener {
            LoginManager.getInstance().logOut()
            startActivity(intentLogIn)
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}