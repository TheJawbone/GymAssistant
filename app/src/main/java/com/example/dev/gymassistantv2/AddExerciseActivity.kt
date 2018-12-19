package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.example.dev.gymassistantv2.database.GymAssistantDatabase
import com.example.dev.gymassistantv2.dtos.UserDto
import com.example.dev.gymassistantv2.entities.Exercise

class AddExerciseActivity : Activity() {

    private lateinit var loggedUser: UserDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercise)

        processIntent()
        setNavigationControls()
        setMuscleGroupSpinner()
    }

    private fun processIntent() {
        this.loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
    }

    private fun setNavigationControls() {
        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val exerciseName = findViewById<EditText>(R.id.editTextExerciseName).text.toString()
            if(exerciseName != null && !exerciseName.isEmpty()) {
                val dbContext = GymAssistantDatabase.getInstance(this)!!
                val muscleGroup = dbContext.muscleGroupDao().getByName(
                        findViewById<Spinner>(R.id.spinnerMuscleGroups).selectedItem.toString()
                )
                var exercise = Exercise()
                exercise.defaultExercise = 0
                exercise.muscleGroupId = muscleGroup.id
                exercise.name = exerciseName
                exercise.ownerId = loggedUser.userId
                exercise.visible = true
                dbContext.exerciseDao().insert(exercise)

                Toast.makeText(this, "Pomyślnie dodano ćwiczenie", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Wprowadź nazwę ćwiczenia", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setMuscleGroupSpinner() {
        var muscleGroupNames = GymAssistantDatabase.getInstance(this)!!.muscleGroupDao().getAllNames()
        val spinnerMuscleGroups = findViewById<Spinner>(R.id.spinnerMuscleGroups)
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, muscleGroupNames)
        spinnerMuscleGroups.adapter = adapter
    }
}