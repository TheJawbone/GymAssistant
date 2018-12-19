package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import com.example.dev.gymassistantv2.database.GymAssistantDatabase
import android.widget.*
import com.example.dev.gymassistantv2.dtos.UserDto
import com.example.dev.gymassistantv2.entities.ExerciseSet


class SetEditActivity : Activity() {

    private lateinit var loggedUser: UserDto
    private var historyOwnerId: Long = 0
    private var set = ExerciseSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_edit)

        processIntent()
        setEditTexts()
        setNavigationControls()
        setFonts()
    }

    private fun processIntent() {
        val setId = this.intent.getLongExtra("setId", 0)
        set = GymAssistantDatabase.getInstance(this)!!.exerciseSetDao().getById(setId)
        this.loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
        this.historyOwnerId = this.intent.getLongExtra("historyOwnerId", historyOwnerId)
    }

    private fun setEditTexts() {
        findViewById<EditText>(R.id.editTextWeight).setText(set.weight.toString())
        findViewById<EditText>(R.id.editTextRepCount).setText(set.repCount.toString())
    }

    private fun setNavigationControls() {
        val buttonSave = findViewById<Button>(R.id.buttonSave)
        buttonSave.setOnClickListener {
            val weight = findViewById<EditText>(R.id.editTextWeight).text.toString()
            val repCount = findViewById<EditText>(R.id.editTextRepCount).text.toString()
            if(weight.isEmpty() || repCount.isEmpty()) {
                Toast.makeText(this, "Uzupełnij oba pola aby kontynuować", Toast.LENGTH_LONG).show()
            } else {
                set.weight = weight.toInt()
                set.repCount = repCount.toInt()
                GymAssistantDatabase.getInstance(this)!!.exerciseSetDao().update(set)
                val intentSave = Intent(this, SetHistoryActivity::class.java)
                intentSave.putExtra("segmentId", set.segmentId)
                intentSave.putExtra("historyOwnerId", historyOwnerId)
                intentSave.putExtra("loggedUser", loggedUser)
                startActivity(intentSave)
            }
        }

        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun setFonts() {
        val typeface = Typeface.createFromAsset(assets, "fonts/BlackOpsOne-Regular.ttf")
        findViewById<Button>(R.id.buttonSave).typeface = typeface
        findViewById<Button>(R.id.buttonBack).typeface = typeface
        findViewById<TextView>(R.id.textViewWeight).typeface = typeface
        findViewById<TextView>(R.id.textViewRepCount).typeface = typeface
    }
}