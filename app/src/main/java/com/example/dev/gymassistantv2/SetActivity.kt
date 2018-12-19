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
        findViewById<TextView>(R.id.textViewSeriesNumber).text = setNumber.toString()
    }

    private fun setNavigationControls() {
        val buttonNextSet = findViewById<Button>(R.id.buttonNextSet)
        val intentNextSet = Intent(this, SetActivity::class.java)
        intentNextSet.putExtra("setNumber", setNumber + 1)
        intentNextSet.putExtra("segmentId", segmentId)
        intentNextSet.putExtra("workoutId", workoutId)
        intentNextSet.putExtra("loggedUser", loggedUser)
        buttonNextSet.setOnClickListener {
            intentNextSet.putExtra("stopwatchElapsedTime", timer.elapsedTime)
            intentNextSet.putExtra("stopwatchRunning", timer.isRunning)
            processSetPersistenceRequest(intentNextSet)
        }

        val buttonNextExerciseSave = findViewById<Button>(R.id.buttonNextExerciseOK)
        val intentNextExerciseSave = Intent(this, ChooseExerciseActivity::class.java)
        intentNextExerciseSave.putExtra("workoutId", workoutId)
        intentNextExerciseSave.putExtra("loggedUser", loggedUser)
        buttonNextExerciseSave.setOnClickListener {
            processSetPersistenceRequest(intentNextExerciseSave)
        }

        val buttonNextExerciseCancel = findViewById<Button>(R.id.buttonNextExerciseCancel)
        val intentNextExerciseCancel = Intent(this, ChooseExerciseActivity::class.java)
        intentNextExerciseCancel.putExtra("workoutId", workoutId)
        intentNextExerciseCancel.putExtra("loggedUser", loggedUser)
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

    private fun persistSet(repCount: Int, weight: Int) {
        val set = ExerciseSet()
        set.repCount = repCount
        set.weight = weight
        set.segmentId = segmentId
        GymAssistantDatabase.getInstance(this)!!.exerciseSetDao().insert(set)
    }
}