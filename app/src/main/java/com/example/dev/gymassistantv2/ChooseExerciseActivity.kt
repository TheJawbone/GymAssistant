package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.example.dev.gymassistantv2.Database.GymAssistantDatabase
import com.example.dev.gymassistantv2.Entities.Segment

class ChooseExerciseActivity : Activity() {

    private var workoutId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_excercise)

        processIntent()
        setNavigationControls()
        setMuscleGroupSpinner()
        setExerciseSpinner()
    }

    override fun onBackPressed() {}

    private fun processIntent() {
        this.intent.getLongExtra("workoutId", workoutId)
    }

    private fun setNavigationControls() {
        val buttonBeginSet = findViewById<Button>(R.id.buttonBeginSet)
        val intentBeginSet = Intent(this, SetActivity::class.java)
        buttonBeginSet.setOnClickListener {
            val dbContext = GymAssistantDatabase.getInstance(this)
            val exercise = dbContext!!.exerciseDao().getByName(findViewById<Spinner>(
                    R.id.spinnerExerciseList).selectedItem.toString())
            val segment = Segment()
            segment.exerciseId = exercise.id
            val segmentId = dbContext!!.segmentDao().insert(segment)
            intentBeginSet.putExtra("segmentId", segmentId)
            startActivity(intentBeginSet)
        }

        val buttonEndWorkout = findViewById<Button>(R.id.buttonEndWorkout)
        val intentEndWorkout = Intent(this, MainMenuActivity::class.java)
        buttonEndWorkout.setOnClickListener { startActivity(intentEndWorkout) }
    }

    private fun setMuscleGroupSpinner() {
        val dbContext = GymAssistantDatabase.getInstance(this)
        val spinner = findViewById<Spinner>(R.id.spinnerMuscleGroups)
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                dbContext!!.muscleGroupDao().getAllNames())
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = spinner.getSelectedItem().toString();
                val muscleGroup = dbContext!!.muscleGroupDao().getByName(selectedItem)
                setExerciseSpinner(muscleGroup.id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }
        }
    }

    private fun setExerciseSpinner(muscleGroupId: Long? = null) {
        val dbContext = GymAssistantDatabase.getInstance(this)
        val spinner = findViewById<Spinner>(R.id.spinnerExerciseList)
        var spinnerElements = listOf<String>()
        if(muscleGroupId == null) {
            spinnerElements = dbContext!!.exerciseDao().getAllNames()
        }
        else {
            spinnerElements = dbContext!!.exerciseDao().getNamesForMuscleGroup(muscleGroupId)
        }
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerElements)
        spinner.adapter = adapter
    }
}
