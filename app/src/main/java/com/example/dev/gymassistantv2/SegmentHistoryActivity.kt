package com.example.dev.gymassistantv2

import android.app.Activity
import android.os.Bundle
import com.example.dev.gymassistantv2.Database.GymAssistantDatabase
import android.content.Intent
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.widget.*
import java.sql.Date
import java.text.SimpleDateFormat


class SegmentHistoryActivity : Activity() {

    private var workoutId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.generic_history_layout)

        processIntent()
        generateSegmentList()
    }

    private fun processIntent() {
        this.workoutId = this.intent.getLongExtra("workoutId", 0)
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
        layout.setPadding(Math.ceil((10 * logicalDensity).toDouble()).toInt(), 0,
                Math.ceil((10 * logicalDensity).toDouble()).toInt(), 0)
        scrollView.addView(layout)

        val dbContext = GymAssistantDatabase.getInstance(this)
        val segments = dbContext!!.segmentDao().getForWorkout(workoutId)

        if(segments.isEmpty()) {
            Toast.makeText(this, "Brak zapisanych segmentów", Toast.LENGTH_LONG).show()
        }

        segments.forEach {
            val exerciseName = dbContext!!.exerciseDao().getById(it.exerciseId!!).name
            val numberOfSets = dbContext!!.exerciseSetDao().getForSegment(it.id!!).count()
            val button = Button(applicationContext)
            button.height = Math.ceil((60 * logicalDensity).toDouble()).toInt()
            button.background = applicationContext.getDrawable(R.drawable.bottom_border)
            button.text = "$exerciseName\n$numberOfSets serii"
            val typeface = Typeface.createFromAsset(assets, "fonts/BlackOpsOne-Regular.ttf")
            button.typeface = typeface
            val segmentId = it.id

            button.setOnClickListener {
                val intent = Intent(applicationContext, SetHistoryActivity::class.java)
                intent.putExtra("segmentId", segmentId)
                startActivity(intent)
            }
            layout.addView(button)
        }
    }
}