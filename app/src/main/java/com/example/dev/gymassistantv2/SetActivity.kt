package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.dev.gymassistantv2.dtos.UserDto
import com.example.dev.gymassistantv2.database.GymAssistantDatabase
import com.example.dev.gymassistantv2.entities.ExerciseSet
import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import com.example.dev.gymassistantv2.utils.Stopwatch


class SetActivity : Activity() {

    private lateinit var loggedUser: UserDto
    private var setNumber: Int = 0
    private var segmentId: Long = 0
    private var workoutId: Long = 0
    private lateinit var repCount: String
    private lateinit var weight: String

    var timer = Stopwatch()
    val REFRESH_RATE = 1.toLong()

    val MSG_START_TIMER = 0
    val MSG_STOP_TIMER = 1
    val MSG_UPDATE_TIMER = 2
    val MSG_RESET_TIMER = 3

    @SuppressLint("HandlerLeak")
    var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val tvTimer = findViewById<TextView>(R.id.textViewTimer)
            when (msg.what) {

                MSG_START_TIMER -> {
                    timer.start()
                    this.sendEmptyMessage(MSG_UPDATE_TIMER)
                }

                MSG_UPDATE_TIMER -> {
                    tvTimer.text = timer.formattedElapsedTime
                    this.sendEmptyMessageDelayed(MSG_UPDATE_TIMER, REFRESH_RATE)
                }

                MSG_STOP_TIMER -> {
                    this.removeMessages(MSG_UPDATE_TIMER)
                    timer.stop()
                    tvTimer.text = timer.formattedElapsedTime
                }

                MSG_RESET_TIMER -> {
                    this.removeMessages(MSG_UPDATE_TIMER)
                    timer.reset()
                    tvTimer.text = timer.formattedElapsedTime
                }

                else -> {
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set)

        processIntent()
        setLayoutTexts()
        setNavigationControls()
        setStopwatchControls()
    }

    private fun processIntent() {
        this.setNumber = this.intent.getIntExtra("setNumber", 1)
        this.segmentId = this.intent.getLongExtra("segmentId", 0)
        this.workoutId = GymAssistantDatabase.getInstance(this)!!.segmentDao().getById(segmentId).workoutId!!
        this.loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
        val stopwatchIntent = intent
        timer.set(stopwatchIntent.getLongExtra("stopwatchElapsedTime", 0))
        val isStopwatchRunning = stopwatchIntent.getBooleanExtra("stopwatchRunning", false)
        if (isStopwatchRunning) {
            mHandler.sendEmptyMessage(MSG_START_TIMER)
        }
    }

    private fun setLayoutTexts() {
        val dbContext = GymAssistantDatabase.getInstance(this)
        val segment = dbContext!!.segmentDao().getById(segmentId)
        val exercise = dbContext.exerciseDao().getById(segment.exerciseId!!)
        findViewById<TextView>(R.id.textViewExerciseName).text = exercise.name
    }

    private fun setNavigationControls() {
        findViewById<EditText>(R.id.editTextRepCount).text.clear()
        findViewById<EditText>(R.id.editTextWeight).text.clear()
        findViewById<TextView>(R.id.textViewSeriesNumber).text = setNumber.toString()
        val buttonNextSet = findViewById<Button>(R.id.buttonNextSet)
        buttonNextSet.setOnClickListener {
            repCount = findViewById<EditText>(R.id.editTextRepCount).text.toString()
            weight = findViewById<EditText>(R.id.editTextWeight).text.toString()
            if (repCount.isNotEmpty() && weight.isNotEmpty()) {
                setNumber+=1
                processSetPersistenceRequest()
                setNavigationControls()
            } else
                Toast.makeText(this, "Uzupełnij oba pola aby kontynuować", Toast.LENGTH_LONG).show()
        }

        val buttonNextExerciseSave = findViewById<Button>(R.id.buttonNextExerciseOK)
        buttonNextExerciseSave.setOnClickListener {
            repCount = findViewById<EditText>(R.id.editTextRepCount).text.toString()
            weight = findViewById<EditText>(R.id.editTextWeight).text.toString()
            if (repCount.isNotEmpty() && weight.isNotEmpty()) {
                processSetPersistenceRequest()
                Toast.makeText(this, "Ćwiczenie zakończone!", Toast.LENGTH_LONG).show()
                finish()
            } else
                Toast.makeText(this, "Uzupełnij oba pola aby kontynuować", Toast.LENGTH_LONG).show()
        }

        val buttonNextExerciseCancel = findViewById<Button>(R.id.buttonNextExerciseCancel)
        buttonNextExerciseCancel.setOnClickListener {
            Toast.makeText(this, "Ćwiczenie zakończone!", Toast.LENGTH_LONG).show()
                finish ()
        }
    }

    private fun processSetPersistenceRequest() {
            persistSet()
    }

    private fun setStopwatchControls() {
        findViewById<Button>(R.id.buttonStart).setOnClickListener {
            mHandler.sendEmptyMessage(MSG_START_TIMER)
        }

        findViewById<Button>(R.id.buttonStop).setOnClickListener {
            mHandler.sendEmptyMessage(MSG_STOP_TIMER)
        }

        findViewById<Button>(R.id.buttonReset).setOnClickListener {
            mHandler.sendEmptyMessage(MSG_RESET_TIMER)
        }
    }

    private fun persistSet() {
        val set = ExerciseSet()
        set.repCount = repCount.toInt()
        set.weight = weight.toInt()
        set.segmentId = segmentId
        GymAssistantDatabase.getInstance(this)!!.exerciseSetDao().insert(set)
    }

    override fun onBackPressed() {
        finish()
    }
}