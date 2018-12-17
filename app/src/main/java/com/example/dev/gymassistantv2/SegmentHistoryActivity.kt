package com.example.dev.gymassistantv2

import android.app.Activity
import android.os.Bundle
import com.example.dev.gymassistantv2.Database.GymAssistantDatabase
import android.content.Intent
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.*
import com.example.dev.gymassistantv2.DTOs.UserDto
import java.sql.Date
import java.text.SimpleDateFormat


class SegmentHistoryActivity : Activity() {

    private lateinit var loggedUser: UserDto
    private var historyOwnerId: Long = 0
    private var workoutId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.generic_history_layout)

        processIntent()
        generateSegmentList()
    }

    override fun onBackPressed() {
        val intent = Intent(this, WorkoutHistoryActivity::class.java)
        intent.putExtra("workoutId", workoutId)
        intent.putExtra("loggedUser", loggedUser)
        intent.putExtra("historyOwnerId", historyOwnerId)
        startActivity(intent)
    }

    private fun processIntent() {
        this.workoutId = this.intent.getLongExtra("workoutId", 0)
        this.loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
        this.historyOwnerId = this.intent.getLongExtra("historyOwnerId", historyOwnerId)
    }

    private fun generateSegmentList() {
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val header = findViewById<TextView>(R.id.textViewHeader)
        val workout = GymAssistantDatabase.getInstance(this)!!.workoutDao().getById(workoutId)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm")
        header.text = "Ćwiczenia z treningu " + dateFormat.format(Date(workout.date!!))

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val logicalDensity = metrics.density

        val layout = LinearLayout(applicationContext)
        layout.orientation = LinearLayout.VERTICAL
        scrollView.addView(layout)

        val dbContext = GymAssistantDatabase.getInstance(this)
        val segments = dbContext!!.segmentDao().getForWorkout(workoutId)

        if(segments.isEmpty()) {
            Toast.makeText(this, "Brak zapisanych segmentów", Toast.LENGTH_LONG).show()
        }

        segments.forEach {
            val horizontalLayout = LinearLayout(this)
            horizontalLayout.orientation = LinearLayout.HORIZONTAL
            horizontalLayout.gravity = Gravity.CENTER_VERTICAL

            val exerciseName = dbContext!!.exerciseDao().getById(it.exerciseId!!).name
            val numberOfSets = dbContext!!.exerciseSetDao().getForSegment(it.id!!).count()
            val buttonMain = Button(this)
            buttonMain.layoutParams = LinearLayout.LayoutParams(
                    (metrics.widthPixels * 0.8).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
            buttonMain.background = this.getDrawable(R.drawable.bottom_border)
            buttonMain.text = "$exerciseName\nSerii: $numberOfSets"
            val typeface = Typeface.createFromAsset(assets, "fonts/BlackOpsOne-Regular.ttf")
            buttonMain.typeface = typeface
            buttonMain.measure(windowManager.defaultDisplay.width, windowManager.defaultDisplay.height)
            val segmentId = it.id
            buttonMain.setOnClickListener {
                val intent = Intent(this, SetHistoryActivity::class.java)
                intent.putExtra("segmentId", segmentId)
                intent.putExtra("loggedUser", loggedUser)
                intent.putExtra("historyOwnerId", historyOwnerId)
                startActivity(intent)
            }

            val buttonDelete = Button(this)
            buttonDelete.height = buttonMain.measuredHeight
            buttonDelete.width = (metrics.widthPixels * 0.2).toInt()
            buttonDelete.background = applicationContext.getDrawable(R.drawable.bottom_border_red)
            buttonDelete.text = "Usuń"
            buttonDelete.typeface = typeface
            buttonDelete.setOnClickListener {
                dbContext!!.segmentDao().delete(dbContext!!.segmentDao().getById(segmentId!!))
                val intent = Intent(this, SegmentHistoryActivity::class.java)
                intent.putExtra("workoutId", workoutId)
                intent.putExtra("loggedUser", loggedUser)
                intent.putExtra("historyOwnerId", historyOwnerId)
                startActivity(intent)
            }

            horizontalLayout.addView(buttonMain)
            horizontalLayout.addView(buttonDelete)
            layout.addView(horizontalLayout)
        }
    }
}