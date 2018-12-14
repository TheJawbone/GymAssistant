package com.example.dev.gymassistantv2

import android.app.Activity
import android.os.Bundle
import com.example.dev.gymassistantv2.Database.GymAssistantDatabase
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.widget.*
import java.sql.Date
import java.text.SimpleDateFormat


class SetHistoryActivity : Activity() {

    private var segmentId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.generic_history_layout)

        processIntent()
        generateSegmentList()
    }

    private fun processIntent() {
        this.segmentId = this.intent.getLongExtra("segmentId", 0)
    }

    private fun generateSegmentList() {
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val header = findViewById<TextView>(R.id.textViewHeader)
        header.text = "Serie w ćwiczeniu\n" + GymAssistantDatabase.getInstance(this)!!.exerciseDao().getById(
                GymAssistantDatabase.getInstance(this)!!.segmentDao().getById(segmentId).exerciseId!!
        ).name

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val logicalDensity = metrics.density

        val layout = LinearLayout(applicationContext)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(Math.ceil((10 * logicalDensity).toDouble()).toInt(), 0,
                Math.ceil((10 * logicalDensity).toDouble()).toInt(), 0)
        scrollView.addView(layout)

        val dbContext = GymAssistantDatabase.getInstance(this)
        val segments = dbContext!!.exerciseSetDao().getForSegment(segmentId)

        if(segments.isEmpty()) {
            Toast.makeText(this, "Brak zapisanych serii", Toast.LENGTH_LONG).show()
        }

        segments.forEach {
            val button = Button(applicationContext)
            button.height = Math.ceil((60 * logicalDensity).toDouble()).toInt()
            button.background = applicationContext.getDrawable(R.drawable.bottom_border)
            button.text = "Ciężar: " + it.weight + "\nLiczba powtórzeń: " + it.repCount
            val typeface = Typeface.createFromAsset(assets, "fonts/BlackOpsOne-Regular.ttf")
            button.typeface = typeface
            layout.addView(button)
        }
    }
}