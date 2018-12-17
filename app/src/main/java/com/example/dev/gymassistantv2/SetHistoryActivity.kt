package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.dev.gymassistantv2.Database.GymAssistantDatabase
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.view.Gravity
import android.widget.*
import com.example.dev.gymassistantv2.DTOs.UserDto
import java.sql.Date
import java.text.SimpleDateFormat


class SetHistoryActivity : Activity() {

    private lateinit var loggedUser: UserDto
    private var historyOwnerId: Long = 0
    private var segmentId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.generic_history_layout)

        processIntent()
        generateSegmentList()
    }

    override fun onBackPressed() {
        val intent = Intent(this, SegmentHistoryActivity::class.java)
        intent.putExtra("workoutId",
                GymAssistantDatabase.getInstance(this)!!.segmentDao().getById(segmentId).workoutId)
        intent.putExtra("loggedUser", loggedUser)
        intent.putExtra("historyOwnerId", historyOwnerId)
        startActivity(intent)
    }

    private fun processIntent() {
        this.segmentId = this.intent.getLongExtra("segmentId", 0)
        this.historyOwnerId = this.intent.getLongExtra("historyOwnerId", historyOwnerId)
        this.loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
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
        /*layout.setPadding(Math.ceil((10 * logicalDensity).toDouble()).toInt(), 0,
                Math.ceil((10 * logicalDensity).toDouble()).toInt(), 0)*/
        scrollView.addView(layout)

        val dbContext = GymAssistantDatabase.getInstance(this)
        val segments = dbContext!!.exerciseSetDao().getForSegment(segmentId)

        if(segments.isEmpty()) {
            Toast.makeText(this, "Brak zapisanych serii", Toast.LENGTH_LONG).show()
        }

        segments.forEach {
            val horizontalLayout = LinearLayout(this)
            horizontalLayout.orientation = LinearLayout.HORIZONTAL
            horizontalLayout.gravity = Gravity.CENTER_VERTICAL

            val buttonMain = Button(applicationContext)
            buttonMain.layoutParams = LinearLayout.LayoutParams(
                    (metrics.widthPixels * 0.6).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
            buttonMain.background = applicationContext.getDrawable(R.drawable.bottom_border)
            buttonMain.text = "Ciężar: " + it.weight + "\nLiczba powtórzeń: " + it.repCount
            val typeface = Typeface.createFromAsset(assets, "fonts/BlackOpsOne-Regular.ttf")
            buttonMain.typeface = typeface
            buttonMain.measure(windowManager.defaultDisplay.width, windowManager.defaultDisplay.height)

            val buttonEdit = Button(this)
            buttonEdit.height = buttonMain.measuredHeight
            buttonEdit.width = (metrics.widthPixels * 0.2).toInt()
            buttonEdit.background = applicationContext.getDrawable(R.drawable.bottom_border_blue)
            buttonEdit.text = "Edytuj"
            buttonEdit.typeface = typeface
            val setId = it.id
            buttonEdit.setOnClickListener {
                val intent = Intent(this, SetEditActivity::class.java)
                intent.putExtra("loggedUser", loggedUser)
                intent.putExtra("historyOwnerId", historyOwnerId)
                intent.putExtra("setId", setId)
                startActivity(intent)
            }

            val buttonDelete = Button(this)
            buttonDelete.height = buttonMain.measuredHeight
            buttonDelete.width = (metrics.widthPixels * 0.2).toInt()
            buttonDelete.background = applicationContext.getDrawable(R.drawable.bottom_border_red)
            buttonDelete.text = "Usuń"
            buttonDelete.typeface = typeface
            val exerciseSetId = it.id
            buttonDelete.setOnClickListener {
                dbContext.exerciseSetDao().delete(dbContext.exerciseSetDao().getById(exerciseSetId!!))
                val intent = Intent(this, SetHistoryActivity::class.java)
                intent.putExtra("segmentId", segmentId)
                intent.putExtra("historyOwnerId", historyOwnerId)
                intent.putExtra("loggedUser", loggedUser)
                startActivity(intent)
            }

            horizontalLayout.addView(buttonMain)
            horizontalLayout.addView(buttonEdit)
            horizontalLayout.addView(buttonDelete)
            layout.addView(horizontalLayout)
        }
    }
}