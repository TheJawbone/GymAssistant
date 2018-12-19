package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.dev.gymassistantv2.dtos.UserDto
import com.example.dev.gymassistantv2.database.GymAssistantDatabase
import com.example.dev.gymassistantv2.entities.Segment

class ChooseExerciseActivity : Activity() {

    private lateinit var loggedUser: UserDto
    private var workoutId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_excercise)

        processIntent()
        setNavigationControls()
        setMuscleGroupSpinner()
        setExerciseSpinner()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun processIntent() {
        this.workoutId = this.intent.getLongExtra("workoutId", workoutId)
        loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
    }

    private fun setNavigationControls() {
        val dbContext = GymAssistantDatabase.getInstance(this)

        val buttonBeginSet = findViewById<Button>(R.id.buttonBeginSet)
        val intentBeginSet = Intent(this, SetActivity::class.java)
        buttonBeginSet.setOnClickListener {
            val exercise = dbContext!!.exerciseDao().getByName(findViewById<Spinner>(
                    R.id.spinnerExerciseList).selectedItem.toString())
            val segment = Segment()
            segment.exerciseId = exercise.id
            segment.workoutId = workoutId
            val segmentId = dbContext.segmentDao().insert(segment)
            intentBeginSet.putExtra("segmentId", segmentId)
            intentBeginSet.putExtra("loggedUser", loggedUser)
            startActivity(intentBeginSet)
        }

        val buttonEndWorkout = findViewById<Button>(R.id.buttonEndWorkout)
        buttonEndWorkout.setOnClickListener {
            val workoutSegments = dbContext!!.segmentDao().getForWorkout(workoutId)
            if(workoutSegments.isEmpty()) {
                dbContext.workoutDao().delete(dbContext.workoutDao().getById(workoutId))
            }
            Toast.makeText(this, "Trening zakończony!", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setMuscleGroupSpinner() {
        val dbContext = GymAssistantDatabase.getInstance(this)
        val spinner = findViewById<Spinner>(R.id.spinnerMuscleGroups)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                dbContext!!.muscleGroupDao().getAllNames())
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = spinner.selectedItem.toString()
                val muscleGroup = dbContext.muscleGroupDao().getByName(selectedItem)
                setExerciseSpinner(muscleGroup.id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun setExerciseSpinner(muscleGroupId: Long? = null) {
        val dbContext = GymAssistantDatabase.getInstance(this)
        val spinner = findViewById<Spinner>(R.id.spinnerExerciseList)
        var spinnerElements = if(muscleGroupId == null) {
            dbContext!!.exerciseDao().getAllNames()
        }
        else {
            dbContext!!.exerciseDao().getVisibleNamesForMuscleGroup(muscleGroupId)
        }
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerElements)
        spinner.adapter = adapter
    }
}
