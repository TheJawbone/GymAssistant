package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.dev.gymassistantv2.Database.GymAssistantDatabase
import com.example.dev.gymassistantv2.Entities.ExerciseSet

class SetActivity : Activity() {

    private var setNumber: Int = 0
    private var segmentId: Long = 0
    private var workoutId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set)

        processIntent()
        setLayoutTexts()
        setNavigationControls()
    }

    private fun processIntent() {
        this.setNumber = this.intent.getIntExtra("setNumber", 1)
        this.segmentId = this.intent.getLongExtra("segmentId", 0)
        this.workoutId = GymAssistantDatabase.getInstance(this)!!.segmentDao()
                            .getById(segmentId).workoutId!!
    }

    private fun setLayoutTexts() {
        val dbContext = GymAssistantDatabase.getInstance(this)
        val segment = dbContext!!.segmentDao().getById(segmentId)
        val exercise = dbContext!!.exerciseDao().getById(segment.exerciseId!!)
        findViewById<TextView>(R.id.textViewExerciseName).text = exercise.name
        findViewById<TextView>(R.id.textViewSeriesNumber).text = setNumber.toString()
    }

    private fun setNavigationControls() {
        val buttonNextSet = findViewById<Button>(R.id.buttonNextSet)
        val intentNextSet = Intent(this, SetActivity::class.java)
        intentNextSet.putExtra("setNumber", setNumber + 1)
        intentNextSet.putExtra("segmentId", segmentId)
        intentNextSet.putExtra("workoutId", workoutId)
        buttonNextSet.setOnClickListener {
            processSetPersistenceRequest(intentNextSet)
        }

        val buttonNextExerciseSave = findViewById<Button>(R.id.buttonNextExerciseOK)
        val intentNextExerciseSave = Intent(this, ChooseExerciseActivity::class.java)
        intentNextExerciseSave.putExtra("workoutId", workoutId)
        buttonNextExerciseSave.setOnClickListener {
            processSetPersistenceRequest(intentNextExerciseSave)
        }

        val buttonNextExerciseCancel = findViewById<Button>(R.id.buttonNextExerciseCancel)
        val intentNextExerciseCancel = Intent(this, ChooseExerciseActivity::class.java)
        intentNextExerciseCancel.putExtra("workoutId", workoutId)
        buttonNextExerciseCancel.setOnClickListener {
            startActivity(intentNextExerciseCancel)
        }
    }

    private fun processSetPersistenceRequest(intent: Intent) {
        val repCount = findViewById<EditText>(R.id.editTextRepCount).text.toString()
        val weight = findViewById<EditText>(R.id.editTextWeight).text.toString()
        if (repCount.isEmpty() || weight.isEmpty()) {
            Toast.makeText(this, "Uzupełnij oba pola aby kontynuować", Toast.LENGTH_LONG).show()
        } else {
            persistSet(repCount.toInt(), weight.toInt())
            startActivity(intent)
        }
    }

    private fun persistSet(repCount: Int, weight: Int) {
        val set = ExerciseSet()
        set.repCount = repCount
        set.weight = weight
        set.segmentId = segmentId
        GymAssistantDatabase.getInstance(this)!!.exerciseSetDao().insert(set)
    }
}