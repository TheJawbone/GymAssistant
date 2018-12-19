package com.example.dev.gymassistantv2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import com.example.dev.gymassistantv2.dtos.UserDto
import com.example.dev.gymassistantv2.database.GymAssistantDatabase
import com.example.dev.gymassistantv2.entities.Measurement

class AddMeasurementsActivity : Activity() {

    private lateinit var loggedUser: UserDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_measurements)

        processIntent()
        setNavigationControls()
        setBodyPartSpinner()
    }

    private fun processIntent() {
        this.loggedUser = this.intent.getSerializableExtra("loggedUser") as UserDto
    }

    private fun setNavigationControls() {
        val buttonSave = findViewById<Button>(R.id.buttonSave)
        val intentSave = Intent(this, ProgressSubmenuActivity::class.java)
        buttonSave.setOnClickListener {
            val dbContext = GymAssistantDatabase.getInstance(this)
            val bodyPart = dbContext!!.muscleGroupDao().getByName(
                    findViewById<Spinner>(R.id.spinnerMuscleGroups).selectedItem.toString()
            )
            val measurement = Measurement()
            measurement.value = findViewById<EditText>(R.id.editTextMeasurement).text.toString().toInt()
            measurement.bodyPartId = bodyPart.id
            measurement.userId = loggedUser.userId
            measurement.date = System.currentTimeMillis()
            dbContext.measurementDao().insert(measurement)
            Toast.makeText(this, "Pomyślnie dodano pomiar", Toast.LENGTH_LONG).show()
            intentSave.putExtra("loggedUser", loggedUser)
            startActivity(intentSave)
        }

        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener { super.onBackPressed() }
    }

    private fun setBodyPartSpinner() {
        val spinner = findViewById<Spinner>(R.id.spinnerMuscleGroups)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                GymAssistantDatabase.getInstance(this)!!.muscleGroupDao().getAllNames())
        spinner.adapter = adapter
    }
}