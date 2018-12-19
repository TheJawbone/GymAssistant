package com.example.dev.gymassistantv2

import android.app.Activity
import android.os.Bundle
import com.example.dev.gymassistantv2.database.GymAssistantDatabase
import android.content.Intent
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.widget.*
import com.example.dev.gymassistantv2.dtos.UserDto
import java.sql.Date
import java.text.SimpleDateFormat


class WorkoutHistoryActivity : Activity() {

    private lateinit var loggedUser: UserDto
    private var historyOwnerId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.generic_history_layout)

        processIntent()
        generateWorkoutList()
    }

    private fun processIntent() {
        this.loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
        this.historyOwnerId = this.intent.getLongExtra("historyOwnerId", historyOwnerId)
    }

    private fun generateWorkoutList() {
        val scrollView = findViewById<ScrollView>(R.id.scrollView)
        val header = findViewById<TextView>(R.id.textViewHeader)
        val dbContext = GymAssistantDatabase.getInstance(this)
        val currentCharge = dbContext!!.userDao().getById(historyOwnerId)
        if(isTrainerInChargesViewOnlyMode()) header.text = "Treningi: ${currentCharge.firstName} ${currentCharge.lastName}"
        else header.text = "Twoje treningi"

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val layout = LinearLayout(applicationContext)
        layout.orientation = LinearLayout.VERTICAL
        scrollView.addView(layout)

        val workouts = dbContext!!.workoutDao().getForUser(historyOwnerId)
        if(workouts.isEmpty())
            Toast.makeText(this, "Brak zapisanych treningów", Toast.LENGTH_LONG).show()

        workouts.forEach {
            val horizontalLayout = LinearLayout(this)
            horizontalLayout.orientation = LinearLayout.HORIZONTAL

            val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm")
            val date = dateFormat.format(Date(it.date!!))
            val typeface = Typeface.createFromAsset(assets, "fonts/BlackOpsOne-Regular.ttf")
            val workoutId = it.id

            val buttonMain = setMainButton(metrics, date, typeface, workoutId)
            val buttonDelete = setDeleteButton(buttonMain, metrics, typeface, dbContext, workoutId)

            horizontalLayout.addView(buttonMain)
            horizontalLayout.addView(buttonDelete)
            if(isTrainerInChargesViewOnlyMode())
                buttonDelete.isEnabled = false
            layout.addView(horizontalLayout)
        }
    }

    private fun setMainButton(metrics: DisplayMetrics, date: String?, typeface: Typeface?, workoutId: Long?): Button {
        val buttonMain = Button(this)
        buttonMain.layoutParams = LinearLayout.LayoutParams(
                (metrics.widthPixels * 0.8).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT)
        buttonMain.background = applicationContext.getDrawable(R.drawable.bottom_border)
        buttonMain.text = date
        buttonMain.typeface = typeface
        buttonMain.setOnClickListener {
            val intent = Intent(this, SegmentHistoryActivity::class.java)
            intent.putExtra("workoutId", workoutId)
            intent.putExtra("historyOwnerId", historyOwnerId)
            intent.putExtra("loggedUser", loggedUser)
            startActivity(intent)
        }
        return buttonMain
    }

    private fun setDeleteButton(buttonMain: Button, metrics: DisplayMetrics, typeface: Typeface?, dbContext: GymAssistantDatabase, workoutId: Long?): Button {
        val buttonDelete = Button(this)
        buttonDelete.height = buttonMain.height
        buttonDelete.width = (metrics.widthPixels * 0.2).toInt()
        buttonDelete.background = applicationContext.getDrawable(R.drawable.bottom_border_red)
        buttonDelete.text = "Usuń"
        buttonDelete.typeface = typeface
        buttonDelete.setOnClickListener {
            dbContext.workoutDao().delete(dbContext.workoutDao().getById(workoutId!!))
            val intent = Intent(this, WorkoutHistoryActivity::class.java)
            intent.putExtra("historyOwnerId", loggedUser.userId!!)
            intent.putExtra("loggedUser", loggedUser)
            startActivity(intent)
        }
        return buttonDelete
    }

    private fun isTrainerInChargesViewOnlyMode() =
            loggedUser.isTrainer!! && loggedUser.userId != historyOwnerId

    override fun onBackPressed() {
        if(isTrainerInChargesViewOnlyMode()) {
            intent = Intent(this, ManageChargesActivity::class.java)
            intent.putExtra("loggedUser", loggedUser)
            startActivity(intent)
        } else {
            intent = Intent(this, MainMenuActivity::class.java)
            intent.putExtra("loggedUser", loggedUser)
            startActivity(intent)
        }
    }

}